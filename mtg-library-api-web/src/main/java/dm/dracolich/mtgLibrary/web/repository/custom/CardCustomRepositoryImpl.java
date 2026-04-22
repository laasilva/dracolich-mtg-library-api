package dm.dracolich.mtgLibrary.web.repository.custom;

import dm.dracolich.mtgLibrary.dto.enums.Legality;
import dm.dracolich.mtgLibrary.dto.records.CardSearchRecord;
import dm.dracolich.mtgLibrary.web.entity.CardEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class CardCustomRepositoryImpl implements CardCustomRepository {
    private final ReactiveMongoTemplate mongoTemplate;

    @Override
    public Mono<Page<CardEntity>> findByFilters(String name, CardSearchRecord search, int page, int size) {
        Criteria combined = buildCriteria(name, search);

        Query query = Query.query(combined);
        PageRequest pageRequest = PageRequest.of(page, size);

        return mongoTemplate.count(Query.query(combined), CardEntity.class)
                .flatMap(total -> mongoTemplate.find(query.with(pageRequest), CardEntity.class)
                        .collectList()
                        .map(list -> new PageImpl<>(list, pageRequest, total)));
    }

    @Override
    public Mono<CardEntity> findRandom(CardSearchRecord search) {
        Criteria combined = buildCriteria(null, search);

        Aggregation aggregation;
        if (combined.equals(new Criteria())) {
            aggregation = Aggregation.newAggregation(Aggregation.sample(1));
        } else {
            aggregation = Aggregation.newAggregation(
                    Aggregation.match(combined),
                    Aggregation.sample(1)
            );
        }

        return mongoTemplate.aggregate(aggregation, "cards", CardEntity.class).next();
    }

    private Criteria buildCriteria(String name, CardSearchRecord search) {
        List<Criteria> filters = new ArrayList<>();

        if (name != null && !name.isBlank())
            filters.add(Criteria.where("name").regex(name, "i"));

        if (search != null) {
            if (search.oracleText() != null && !search.oracleText().isBlank())
                filters.add(eitherFace("oracle_text").regex(search.oracleText(), "i"));

            if (search.colorIdentity() != null && !search.colorIdentity().isEmpty())
                filters.add(eitherFaceGameplay("color_identity").all(search.colorIdentity()));

            if (search.colors() != null && !search.colors().isEmpty())
                filters.add(eitherFaceGameplay("colors").all(search.colors()));

            if (search.types() != null && !search.types().isEmpty())
                filters.add(eitherFace("types").all(search.types()));

            if (search.subtypes() != null && !search.subtypes().isEmpty())
                filters.add(eitherFace("subtypes").all(search.subtypes()));

            if (search.keywords() != null && !search.keywords().isEmpty())
                filters.add(Criteria.where("keywords").all(search.keywords()));

            if (search.legalIn() != null)
                filters.add(Criteria.where("legalities." + search.legalIn().name()).is(Legality.LEGAL));

            if (search.minManaValue() != null || search.maxManaValue() != null) {
                Criteria defaultMana = Criteria.where("default_face.gameplay_property.mana_value");
                Criteria flippedMana = Criteria.where("flipped_face.gameplay_property.mana_value");
                if (search.minManaValue() != null) {
                    defaultMana = defaultMana.gte(search.minManaValue());
                    flippedMana = flippedMana.gte(search.minManaValue());
                }
                if (search.maxManaValue() != null) {
                    defaultMana = defaultMana.lte(search.maxManaValue());
                    flippedMana = flippedMana.lte(search.maxManaValue());
                }
                filters.add(new Criteria().orOperator(defaultMana, flippedMana));
            }

            if (search.rarity() != null)
                filters.add(Criteria.where("rarity").is(search.rarity()));

            if (search.setCode() != null)
                filters.add(Criteria.where("set_code").is(search.setCode()));
        }

        return filters.isEmpty()
                ? new Criteria()
                : new Criteria().andOperator(filters.toArray(Criteria[]::new));
    }

    private static EitherFaceBuilder eitherFace(String field) {
        return new EitherFaceBuilder("default_face." + field, "flipped_face." + field);
    }

    private static EitherFaceBuilder eitherFaceGameplay(String field) {
        return new EitherFaceBuilder(
                "default_face.gameplay_property." + field,
                "flipped_face.gameplay_property." + field
        );
    }

    private record EitherFaceBuilder(String defaultPath, String flippedPath) {
        Criteria regex(String pattern, String options) {
            return new Criteria().orOperator(
                    Criteria.where(defaultPath).regex(pattern, options),
                    Criteria.where(flippedPath).regex(pattern, options)
            );
        }

        Criteria all(Object values) {
            return new Criteria().orOperator(
                    Criteria.where(defaultPath).all(values),
                    Criteria.where(flippedPath).all(values)
            );
        }
    }
}
