package dm.dracolich.mtgLibrary.web.repository;

import dm.dracolich.mtgLibrary.web.entity.CardEntity;
import dm.dracolich.mtgLibrary.web.repository.custom.CardCustomRepository;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import reactor.core.publisher.Mono;

public interface CardRepository extends ReactiveMongoRepository<CardEntity, String>,
        ReactiveQueryByExampleExecutor<CardEntity>, CardCustomRepository {
    Mono<CardEntity> findByNameIgnoreCase(String name);
    Mono<CardEntity> findByOracleId(String oracleId);
}
