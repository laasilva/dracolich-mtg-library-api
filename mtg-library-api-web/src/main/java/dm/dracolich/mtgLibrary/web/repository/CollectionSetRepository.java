package dm.dracolich.mtgLibrary.web.repository;

import dm.dracolich.mtgLibrary.web.entity.CollectionSetEntity;
import dm.dracolich.mtgLibrary.web.repository.custom.CollectionSetCustomRepository;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

public interface CollectionSetRepository extends ReactiveMongoRepository<CollectionSetEntity, String>,
        CollectionSetCustomRepository {
    Mono<CollectionSetEntity> findByCode(String code);
    Mono<CollectionSetEntity> findByScryfallId(String scryfallId);
}
