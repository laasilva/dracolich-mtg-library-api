package dm.dracolich.mtgLibrary.web.repository.custom;

import dm.dracolich.mtgLibrary.dto.records.SetSearchRecord;
import dm.dracolich.mtgLibrary.web.entity.CollectionSetEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class CollectionSetCustomRepositoryImpl implements CollectionSetCustomRepository {
    private final ReactiveMongoTemplate mongoTemplate;

    @Override
    public Mono<Page<CollectionSetEntity>> findByFilters(String name, SetSearchRecord search, int page, int size) {
        Criteria combined = buildCriteria(name, search);

        Query query = Query.query(combined);
        PageRequest pageRequest = PageRequest.of(page, size);

        return mongoTemplate.count(Query.query(combined), CollectionSetEntity.class)
                .flatMap(total -> mongoTemplate.find(query.with(pageRequest), CollectionSetEntity.class)
                        .collectList()
                        .map(list -> new PageImpl<>(list, pageRequest, total)));
    }

    private Criteria buildCriteria(String name, SetSearchRecord search) {
        List<Criteria> filters = new ArrayList<>();

        if (name != null && !name.isBlank())
            filters.add(Criteria.where("name").regex(name, "i"));

        if (search != null) {
            if (search.code() != null && !search.code().isBlank())
                filters.add(Criteria.where("code").is(search.code()));

            if (search.block() != null && !search.block().isBlank())
                filters.add(Criteria.where("block").regex(search.block(), "i"));

            if (search.collectionSetType() != null)
                filters.add(Criteria.where("collection_set_type").is(search.collectionSetType()));

            if (search.releasedAfter() != null)
                filters.add(Criteria.where("release_date").gte(search.releasedAfter()));

            if (search.releasedBefore() != null)
                filters.add(Criteria.where("release_date").lte(search.releasedBefore()));

            if (search.digital() != null)
                filters.add(Criteria.where("digital").is(search.digital()));
        }

        return filters.isEmpty()
                ? new Criteria()
                : new Criteria().andOperator(filters.toArray(Criteria[]::new));
    }
}
