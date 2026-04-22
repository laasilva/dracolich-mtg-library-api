package dm.dracolich.mtgLibrary.integration.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Map;

// Scryfall card object — flat structure with all printings info.
// See: https://scryfall.com/docs/api/cards
// TODO: Fill in remaining fields from Scryfall docs.
@JsonIgnoreProperties(ignoreUnknown = true)
public record ScryfallCardDto(
        // Core fields
        String id,
        @JsonProperty("oracle_id") String oracleId,
        String name,
        String lang,
        String layout,

        // Gameplay
        @JsonProperty("mana_cost") String manaCost,
        @JsonProperty("cmc") Double cmc,
        @JsonProperty("type_line") String typeLine,
        @JsonProperty("oracle_text") String oracleText,
        @JsonProperty("flavor_text") String flavorText,
        List<String> colors,
        @JsonProperty("color_identity") List<String> colorIdentity,
        List<String> keywords,
        String power,
        String toughness,
        String loyalty,
        String defense,
        @JsonProperty("life_modifier") String lifeModifier,
        @JsonProperty("hand_modifier") String handModifier,
        @JsonProperty("produced_mana") List<String> producedMana,
        Map<String, String> legalities,

        // Print fields
        @JsonProperty("set") String setCode,
        @JsonProperty("set_id") String setId,
        @JsonProperty("set_name") String setName,
        @JsonProperty("collector_number") String collectorNumber,
        String rarity,
        @JsonProperty("flavor_name") String flavorName,
        String watermark,
        @JsonProperty("border_color") String borderColor,
        String frame,
        @JsonProperty("frame_effects") List<String> frameEffects,
        @JsonProperty("full_art") Boolean fullArt,
        Boolean textless,
        Boolean booster,
        Boolean digital,
        List<String> finishes,
        List<String> games,
        Boolean reserved,
        @JsonProperty("promo_types") List<String> promoTypes,
        @JsonProperty("released_at") String releasedAt,

        // Art
        String artist,
        @JsonProperty("artist_ids") List<String> artistIds,
        @JsonProperty("illustration_id") String illustrationId,
        @JsonProperty("image_uris") Map<String, String> imageUris,
        @JsonProperty("highres_image") Boolean highresImage,

        // Prices & purchase
        Map<String, String> prices,
        @JsonProperty("purchase_uris") Map<String, String> purchaseUris,

        // Multi-face
        @JsonProperty("card_faces") List<ScryfallCardFaceDto> cardFaces,

        // Related
        @JsonProperty("all_parts") List<ScryfallRelatedCardDto> allParts,

        // Misc
        @JsonProperty("edhrec_rank") Integer edhrecRank,
        @JsonProperty("penny_rank") Integer pennyRank,
        @JsonProperty("content_warning") Boolean contentWarning,
        @JsonProperty("variation") Boolean variation,
        @JsonProperty("variation_of") String variationOf,
        @JsonProperty("game_changer") Boolean gameChanger
) {}
