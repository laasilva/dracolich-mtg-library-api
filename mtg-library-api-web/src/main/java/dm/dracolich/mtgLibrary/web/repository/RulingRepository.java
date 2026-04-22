package dm.dracolich.mtgLibrary.web.repository;

import dm.dracolich.mtgLibrary.web.entity.RulingEntity;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

public interface RulingRepository extends ReactiveMongoRepository<RulingEntity, String> {
    Flux<RulingEntity> findByOracleId(String oracleId);
}
