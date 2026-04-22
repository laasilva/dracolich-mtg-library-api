package dm.dracolich.mtgLibrary.web.repository;

import dm.dracolich.mtgLibrary.web.entity.CardLayoutEntity;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

public interface CardLayoutRepository extends ReactiveMongoRepository<CardLayoutEntity, String> {
    Mono<CardLayoutEntity> findByCode(String code);
}
