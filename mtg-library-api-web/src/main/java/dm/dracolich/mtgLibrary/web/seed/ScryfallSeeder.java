package dm.dracolich.mtgLibrary.web.seed;

import dm.dracolich.mtgLibrary.dto.enums.*;
import dm.dracolich.mtgLibrary.integration.client.ScryfallClient;
import dm.dracolich.mtgLibrary.integration.dto.*;
import dm.dracolich.mtgLibrary.web.entity.*;
import dm.dracolich.mtgLibrary.web.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class ScryfallSeeder {

    private volatile boolean syncing = false;

    private final ScryfallClient scryfallClient;
    private final ReactiveMongoTemplate mongoTemplate;
    private final CollectionSetRepository setRepository;
    private final SymbolRepository symbolRepository;
    private final CardRepository cardRepository;
    private final ArtPropertyRepository artPropertyRepository;
    private final RulingRepository rulingRepository;
    private final CardLayoutRepository layoutRepository;
    private final FrameEffectRepository frameEffectRepository;
    private final SubtypeRepository subtypeRepository;

    @EventListener(ApplicationReadyEvent.class)
    public void seed() {
        // Only seed if the database is empty.
        cardRepository.count()
                .flatMap(count -> {
                    if (count > 0) {
                        log.info("Database already seeded ({} cards). Skipping.", count);
                        return Mono.empty();
                    }
                    log.info("Database empty. Starting Scryfall seed...");
                    return sync();
                })
                .subscribe(
                        unused -> {},
                        error -> log.error("Seed failed", error),
                        () -> log.info("Seed check complete.")
                );
    }

    public Mono<Void> sync() {
        if (syncing) {
            return Mono.error(new IllegalStateException("Sync already in progress."));
        }
        syncing = true;
        log.info("Starting Scryfall sync...");
        return seedSets()
                .then(seedSymbols())
                .then(seedCards())
                .then(seedRulings())
                .doOnSuccess(v -> log.info("Sync complete."))
                .doFinally(signal -> syncing = false);
    }

    // -----------------------------------------------------------------------
    // Step 1: Sets — GET /sets
    // -----------------------------------------------------------------------
    private Mono<Void> seedSets() {
        return scryfallClient.getSets()
                .flatMapMany(list -> Flux.fromIterable(list.data()))
                .map(this::mapSet)
                .concatMap(entity -> upsert("scryfall_id", entity.getScryfallId(), entity))
                .count()
                .doOnNext(count -> log.info("Sets synced: {}.", count))
                .then();
    }

    // -----------------------------------------------------------------------
    // Step 2: Symbols — GET /symbology
    // -----------------------------------------------------------------------
    private Mono<Void> seedSymbols() {
        return scryfallClient.getSymbology()
                .flatMapMany(list -> Flux.fromIterable(list.data()))
                .map(this::mapSymbol)
                .concatMap(entity -> upsert("symbol", entity.getSymbol(), entity))
                .count()
                .doOnNext(count -> log.info("Symbols synced: {}.", count))
                .then();
    }

    // -----------------------------------------------------------------------
    // Step 3: Cards — bulk data download (oracle_cards or default_cards)
    //
    // Each Scryfall card is FLAT. Your model splits it into:
    //   - CardEntity (oracle-level: name, keywords, legalities, faces)
    //   - ArtPropertyEntity (printing-level: images, prices, rarity, set)
    //   - CardLayoutEntity (discovered from card.layout)
    //   - FrameEffectEntity (discovered from card.frame_effects)
    //   - SubtypeEntity (discovered from parsing type_line)
    //
    // The mapping methods below are where you do this split.
    // -----------------------------------------------------------------------
    private Mono<Void> seedCards() {
        return scryfallClient.getBulkDataList()
                .map(list -> list.data().stream()
                        .filter(bd -> "oracle_cards".equals(bd.type()))
                        .findFirst()
                        .orElseThrow(() -> new IllegalStateException("No oracle_cards bulk data found")))
                .flatMap(bulkData -> {
                    log.info("Downloading oracle_cards from Scryfall...");
                    return scryfallClient.streamBulkCards(bulkData.downloadUri()).collectList();
                })
                .flatMap(scryfallCards -> {
                    log.info("Downloaded {} cards from Scryfall.", scryfallCards.size());
                    return processCards(scryfallCards);
                });
    }

    private Mono<Void> processCards(List<ScryfallCardDto> scryfallCards) {
        // 1. Discover and persist lookup entities
        return persistLayouts(scryfallCards)
                .then(persistFrameEffects(scryfallCards))
                .then(persistSubtypes(scryfallCards))
                .then(Mono.defer(() -> {
                    // 2. Map and upsert cards, collecting oracleId → MongoDB id mapping
                    return Flux.fromIterable(scryfallCards)
                            .map(this::mapCard)
                            .concatMap(card -> upsert("oracle_id", card.getOracleId(), card))
                            .collectList()
                            .doOnNext(cards -> log.info("Cards synced: {}.", cards.size()));
                }))
                .flatMap(savedCards -> {
                    // 3. Build lookup maps: oracleId → card MongoDB id, scryfallSetId → set MongoDB id
                    Map<String, String> oracleIdToCardId = savedCards.stream()
                            .collect(Collectors.toMap(CardEntity::getOracleId, CardEntity::getId));

                    return setRepository.findAll()
                            .collectList()
                            .map(sets -> sets.stream()
                                    .collect(Collectors.toMap(CollectionSetEntity::getScryfallId, CollectionSetEntity::getId)))
                            .flatMap(scryfallSetIdToSetId -> {
                                // 4. Map and upsert art properties using resolved MongoDB ids
                                return Flux.fromIterable(scryfallCards)
                                        .map(dto -> mapArtProperty(dto, oracleIdToCardId, scryfallSetIdToSetId))
                                        .concatMap(art -> upsert("scryfall_id", art.getScryfallId(), art))
                                        .count()
                                        .doOnNext(count -> log.info("Art properties synced: {}.", count));
                            });
                })
                .then();
    }

    // -----------------------------------------------------------------------
    // Lookup entity discovery
    // -----------------------------------------------------------------------

    private Mono<Void> persistLayouts(List<ScryfallCardDto> cards) {
        List<CardLayoutEntity> layouts = cards.stream()
                .map(ScryfallCardDto::layout)
                .filter(Objects::nonNull)
                .distinct()
                .map(layout -> CardLayoutEntity.builder()
                        .code(layout)
                        .name(layout.replace("_", " "))
                        .build())
                .toList();

        return Flux.fromIterable(layouts)
                .concatMap(entity -> upsert("code", entity.getCode(), entity))
                .count()
                .doOnNext(count -> log.info("Layouts synced: {}.", count))
                .then();
    }

    private Mono<Void> persistFrameEffects(List<ScryfallCardDto> cards) {
        List<FrameEffectEntity> effects = cards.stream()
                .map(ScryfallCardDto::frameEffects)
                .filter(Objects::nonNull)
                .flatMap(List::stream)
                .distinct()
                .map(effect -> FrameEffectEntity.builder()
                        .code(effect)
                        .name(effect.replace("_", " "))
                        .build())
                .toList();

        return Flux.fromIterable(effects)
                .concatMap(entity -> upsert("code", entity.getCode(), entity))
                .count()
                .doOnNext(count -> log.info("Frame effects synced: {}.", count))
                .then();
    }

    private Mono<Void> persistSubtypes(List<ScryfallCardDto> cards) {
        Set<SubtypeEntity> subtypes = new LinkedHashSet<>();

        for (ScryfallCardDto card : cards) {
            collectSubtypesFromTypeLine(card.typeLine(), subtypes);
            if (card.cardFaces() != null) {
                for (ScryfallCardFaceDto face : card.cardFaces()) {
                    collectSubtypesFromTypeLine(face.typeLine(), subtypes);
                }
            }
        }

        return Flux.fromIterable(subtypes)
                .concatMap(entity -> upsert("code", entity.getCode(), entity))
                .count()
                .doOnNext(count -> log.info("Subtypes synced: {}.", count))
                .then();
    }

    private void collectSubtypesFromTypeLine(String typeLine, Set<SubtypeEntity> subtypes) {
        if (typeLine == null || !typeLine.contains("\u2014")) return;

        String[] halves = typeLine.split("\\s*\u2014\\s*", 2);
        Set<CardType> types = parseTypes(halves[0]);
        String[] subtypeNames = halves[1].trim().split("\\s+");

        // Assign each subtype to the most specific type that has a SubtypeCategory.
        // Priority: Creature > Planeswalker > Land > Enchantment > Artifact > etc.
        SubtypeCategory category = resolveSubtypeCategory(types);
        if (category == null) return;

        for (String name : subtypeNames) {
            if (name.isBlank()) continue;
            String code = category.name().toLowerCase() + "-" + name.toLowerCase();
            subtypes.add(SubtypeEntity.builder()
                    .code(code)
                    .name(name)
                    .category(category)
                    .build());
        }
    }

    private SubtypeCategory resolveSubtypeCategory(Set<CardType> types) {
        // Order matters: creature subtypes are the most common, check first.
        if (types.contains(CardType.CREATURE)) return SubtypeCategory.CREATURE;
        if (types.contains(CardType.PLANESWALKER)) return SubtypeCategory.PLANESWALKER;
        if (types.contains(CardType.LAND)) return SubtypeCategory.LAND;
        if (types.contains(CardType.ENCHANTMENT)) return SubtypeCategory.ENCHANTMENT;
        if (types.contains(CardType.ARTIFACT)) return SubtypeCategory.ARTIFACT;
        if (types.contains(CardType.BATTLE)) return SubtypeCategory.BATTLE;
        if (types.contains(CardType.INSTANT)) return SubtypeCategory.SPELL;
        if (types.contains(CardType.SORCERY)) return SubtypeCategory.SPELL;
        if (types.contains(CardType.DUNGEON)) return SubtypeCategory.DUNGEON;
        if (types.contains(CardType.PLANE)) return SubtypeCategory.PLANE;
        return null;
    }

    // -----------------------------------------------------------------------
    // Card mapping — split flat Scryfall card into CardEntity + ArtPropertyEntity
    // -----------------------------------------------------------------------

    private CardEntity mapCard(ScryfallCardDto dto) {
        boolean multiface = dto.cardFaces() != null && !dto.cardFaces().isEmpty();

        CardFaceEntity defaultFace;
        CardFaceEntity flippedFace = null;

        if (multiface) {
            defaultFace = mapCardFace(dto.cardFaces().getFirst(), dto.typeLine());
            if (dto.cardFaces().size() > 1) {
                flippedFace = mapCardFace(dto.cardFaces().get(1), null);
            }
        } else {
            defaultFace = mapCardFaceFromTopLevel(dto);
        }

        List<RelatedCardPartEntity> allParts = null;
        if (dto.allParts() != null) {
            allParts = dto.allParts().stream()
                    .map(this::mapRelatedPart)
                    .toList();
        }

        return CardEntity.builder()
                .oracleId(dto.oracleId())
                .name(dto.name())
                .keywords(dto.keywords() != null ? new LinkedHashSet<>(dto.keywords()) : null)
                .legalities(parseLegalities(dto.legalities()))
                .layout(dto.layout())
                .reserved(dto.reserved())
                .firstReleaseDate(parseDate(dto.releasedAt()))
                .edhrecRank(dto.edhrecRank())
                .pennyRank(dto.pennyRank())
                .multiface(multiface)
                .defaultFace(defaultFace)
                .flippedFace(flippedFace)
                .allParts(allParts)
                .build();
    }

    private CardFaceEntity mapCardFaceFromTopLevel(ScryfallCardDto dto) {
        return CardFaceEntity.builder()
                .name(dto.name())
                .fullType(dto.typeLine())
                .types(parseTypes(dto.typeLine()))
                .supertypes(parseSupertypes(dto.typeLine()))
                .subtypes(parseSubtypeIds(dto.typeLine()))
                .oracleText(dto.oracleText())
                .flavorText(dto.flavorText())
                .gameplayProperty(GameplayPropertyEntity.builder()
                        .manaCost(dto.manaCost())
                        .manaValue(dto.cmc())
                        .colorIdentity(parseColors(dto.colorIdentity()))
                        .colors(parseColors(dto.colors()))
                        .power(dto.power())
                        .toughness(dto.toughness())
                        .defense(dto.defense())
                        .loyalty(dto.loyalty())
                        .lifeModifier(dto.lifeModifier())
                        .handModifier(dto.handModifier())
                        .producedMana(parseColors(dto.producedMana()))
                        .gameChanger(dto.gameChanger())
                        .build())
                .build();
    }

    private CardFaceEntity mapCardFace(ScryfallCardFaceDto face, String fallbackTypeLine) {
        String typeLine = face.typeLine() != null ? face.typeLine() : fallbackTypeLine;
        return CardFaceEntity.builder()
                .name(face.name())
                .fullType(typeLine)
                .types(parseTypes(typeLine))
                .supertypes(parseSupertypes(typeLine))
                .subtypes(parseSubtypeIds(typeLine))
                .oracleText(face.oracleText())
                .flavorText(face.flavorText())
                .gameplayProperty(GameplayPropertyEntity.builder()
                        .manaCost(face.manaCost())
                        .colors(parseColors(face.colors()))
                        .power(face.power())
                        .toughness(face.toughness())
                        .defense(face.defense())
                        .loyalty(face.loyalty())
                        .build())
                .build();
    }

    private RelatedCardPartEntity mapRelatedPart(ScryfallRelatedCardDto dto) {
        return RelatedCardPartEntity.builder()
                .relatedCardId(dto.id())
                .component(parseEnum(RelatedCardComponent.class, dto.component()))
                .name(dto.name())
                .typeLine(dto.typeLine())
                .build();
    }

    // -----------------------------------------------------------------------
    // ArtProperty mapping — printing-level data
    // -----------------------------------------------------------------------

    private ArtPropertyEntity mapArtProperty(ScryfallCardDto dto,
                                               Map<String, String> oracleIdToCardId,
                                               Map<String, String> scryfallSetIdToSetId) {
        return ArtPropertyEntity.builder()
                .scryfallId(dto.id())
                .cardId(oracleIdToCardId.get(dto.oracleId()))
                .setId(scryfallSetIdToSetId.getOrDefault(dto.setId(), dto.setId()))
                .artistIds(dto.artistIds() != null ? new LinkedHashSet<>(dto.artistIds()) : null)
                .variationOf(dto.variationOf())
                .releaseDate(parseDate(dto.releasedAt()))
                .rarity(parseEnum(Rarity.class, dto.rarity()))
                .hiResImage(dto.highresImage())
                .imageUris(dto.imageUris())
                .prices(parsePrices(dto.prices()))
                .purchaseUris(dto.purchaseUris())
                .booster(dto.booster())
                .collectorNumber(dto.collectorNumber())
                .contentWarning(dto.contentWarning())
                .digitalOnly(dto.digital())
                .finishes(parseEnumSet(CardFinish.class, dto.finishes()))
                .flavorName(dto.flavorName())
                .flavorText(dto.flavorText())
                .games(parseEnumSet(Game.class, dto.games()))
                .lang(parseEnum(Language.class, dto.lang()))
                .borderColor(parseEnum(BorderColor.class, dto.borderColor()))
                .frame(parseFrame(dto.frame()))
                .frameEffects(dto.frameEffects() != null ? new LinkedHashSet<>(dto.frameEffects()) : null)
                .promoTypes(dto.promoTypes() != null ? new LinkedHashSet<>(dto.promoTypes()) : null)
                .fullArt(dto.fullArt())
                .textless(dto.textless())
                .variation(dto.variation())
                .watermark(dto.watermark())
                .illustrationId(dto.illustrationId())
                .build();
    }

    // -----------------------------------------------------------------------
    // Step 4: Rulings — bulk data download
    // -----------------------------------------------------------------------
    private Mono<Void> seedRulings() {
        // Drop and re-insert rulings — they have no stable external ID for upsert,
        // and the full set is small enough to replace on each sync.
        return rulingRepository.deleteAll()
                .then(scryfallClient.getBulkDataList()
                        .map(list -> list.data().stream()
                                .filter(bd -> "rulings".equals(bd.type()))
                                .findFirst()
                                .orElseThrow(() -> new IllegalStateException("No rulings bulk data found")))
                        .flatMapMany(bulkData -> scryfallClient.streamBulkRulings(bulkData.downloadUri()))
                        .map(this::mapRuling)
                        .collectList()
                        .flatMapMany(rulingRepository::saveAll)
                        .count()
                        .doOnNext(count -> log.info("Rulings synced: {}.", count))
                        .then());
    }

    // -----------------------------------------------------------------------
    // Set / Symbol / Ruling mapping
    // -----------------------------------------------------------------------

    private CollectionSetEntity mapSet(ScryfallSetDto dto) {
        return CollectionSetEntity.builder()
                .scryfallId(dto.id())
                .name(dto.name())
                .code(dto.code())
                .parentSetCode(dto.parentSetCode())
                .block(dto.block())
                .collectionSetType(parseSetType(dto.setType()))
                .releaseDate(parseDate(dto.releasedAt()))
                .iconSvgUri(dto.iconSvgUri())
                .cardCount(dto.cardCount())
                .digital(dto.digital())
                .foilOnly(dto.foilOnly())
                .nonFoilOnly(dto.nonFoilOnly())
                .build();
    }

    private SymbolEntity mapSymbol(ScryfallSymbolDto dto) {
        return SymbolEntity.builder()
                .symbol(dto.symbol())
                .plaintext(dto.english())
                .alt(dto.looseVariant())
                .representsMana(dto.representsMana())
                .manaValue(dto.manaValue() != null ? dto.manaValue().intValue() : null)
                .svgUri(dto.svgUri())
                .build();
    }

    private RulingEntity mapRuling(ScryfallRulingDto dto) {
        return RulingEntity.builder()
                .oracleId(dto.oracleId())
                .source(dto.source())
                .published(parseDate(dto.publishedAt()))
                .comment(dto.comment())
                .build();
    }

    private CollectionSetType parseSetType(String scryfallType) {
        if (scryfallType == null) return null;
        try {
            return CollectionSetType.valueOf(scryfallType.toUpperCase());
        } catch (IllegalArgumentException e) {
            log.warn("Unknown set type '{}', skipping enum mapping.", scryfallType);
            return null;
        }
    }

    private LocalDate parseDate(String dateStr) {
        if (dateStr == null || dateStr.isBlank()) return null;
        return LocalDate.parse(dateStr);
    }

    // -----------------------------------------------------------------------
    // Type line parsing
    // -----------------------------------------------------------------------

    private Set<CardType> parseTypes(String typeLine) {
        if (typeLine == null) return Set.of();
        // Only parse the left side of the em dash (types/supertypes, not subtypes).
        String typesPart = typeLine.contains("\u2014")
                ? typeLine.split("\\s*\u2014\\s*", 2)[0]
                : typeLine;
        return Arrays.stream(typesPart.split("\\s+"))
                .map(word -> parseEnum(CardType.class, word))
                .filter(Objects::nonNull)
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    private Set<Supertype> parseSupertypes(String typeLine) {
        if (typeLine == null) return Set.of();
        String typesPart = typeLine.contains("\u2014")
                ? typeLine.split("\\s*\u2014\\s*", 2)[0]
                : typeLine;
        return Arrays.stream(typesPart.split("\\s+"))
                .map(word -> parseEnum(Supertype.class, word))
                .filter(Objects::nonNull)
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    private Set<String> parseSubtypeIds(String typeLine) {
        if (typeLine == null || !typeLine.contains("\u2014")) return Set.of();
        String[] halves = typeLine.split("\\s*\u2014\\s*", 2);
        Set<CardType> types = parseTypes(halves[0]);
        SubtypeCategory category = resolveSubtypeCategory(types);
        if (category == null) return Set.of();

        return Arrays.stream(halves[1].trim().split("\\s+"))
                .filter(s -> !s.isBlank())
                .map(name -> category.name().toLowerCase() + "-" + name.toLowerCase())
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    // -----------------------------------------------------------------------
    // Enum & color parsing
    // -----------------------------------------------------------------------

    private Set<Color> parseColors(List<String> colorCodes) {
        if (colorCodes == null || colorCodes.isEmpty()) return Set.of();
        return colorCodes.stream()
                .map(code -> parseEnum(Color.class, code))
                .filter(Objects::nonNull)
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    private Map<Format, Legality> parseLegalities(Map<String, String> raw) {
        if (raw == null) return Map.of();
        Map<Format, Legality> result = new LinkedHashMap<>();
        for (Map.Entry<String, String> entry : raw.entrySet()) {
            Format format = parseEnum(Format.class, entry.getKey());
            Legality legality = parseEnum(Legality.class, entry.getValue());
            if (format != null && legality != null) {
                result.put(format, legality);
            }
        }
        return result;
    }

    private <E extends Enum<E>> E parseEnum(Class<E> enumClass, String value) {
        if (value == null) return null;
        try {
            return Enum.valueOf(enumClass, value.toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    private <E extends Enum<E>> Set<E> parseEnumSet(Class<E> enumClass, List<String> values) {
        if (values == null || values.isEmpty()) return Set.of();
        return values.stream()
                .map(v -> parseEnum(enumClass, v))
                .filter(Objects::nonNull)
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    private Frame parseFrame(String frame) {
        if (frame == null) return null;
        // Scryfall sends "1993", "1997", "2003", "2015", "future".
        // Our enum uses FRAME_1993, FRAME_1997, etc.
        return switch (frame) {
            case "1993" -> Frame.FRAME_1993;
            case "1997" -> Frame.FRAME_1997;
            case "2003" -> Frame.FRAME_2003;
            case "2015" -> Frame.FRAME_2015;
            case "future" -> Frame.FUTURE;
            default -> null;
        };
    }

    private Map<String, Double> parsePrices(Map<String, String> raw) {
        if (raw == null) return Map.of();
        Map<String, Double> result = new LinkedHashMap<>();
        for (Map.Entry<String, String> entry : raw.entrySet()) {
            if (entry.getValue() != null) {
                try {
                    result.put(entry.getKey(), Double.parseDouble(entry.getValue()));
                } catch (NumberFormatException ignored) {}
            }
        }
        return result;
    }

    // -----------------------------------------------------------------------
    // Generic upsert — find by natural key, update if exists, insert if not
    // -----------------------------------------------------------------------

    private <T> Mono<T> upsert(String keyField, Object keyValue, T entity) {
        return mongoTemplate.findOne(
                        Query.query(Criteria.where(keyField).is(keyValue)),
                        (Class<T>) entity.getClass())
                .flatMap(existing -> {
                    // Copy the existing MongoDB id onto the new entity so save() updates it
                    try {
                        var idField = entity.getClass().getDeclaredField("id");
                        idField.setAccessible(true);
                        idField.set(entity, idField.get(existing));
                    } catch (NoSuchFieldException | IllegalAccessException e) {
                        return Mono.error(e);
                    }
                    return mongoTemplate.save(entity);
                })
                .switchIfEmpty(Mono.defer(() -> mongoTemplate.save(entity)));
    }
}
