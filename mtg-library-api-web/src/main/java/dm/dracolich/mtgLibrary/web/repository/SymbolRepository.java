package dm.dracolich.mtgLibrary.web.repository;

import dm.dracolich.mtgLibrary.web.entity.SymbolEntity;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

public interface SymbolRepository extends ReactiveMongoRepository<SymbolEntity, String> {
    Mono<SymbolEntity> findBySymbol(String symbol);
}
