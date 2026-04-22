package dm.dracolich.mtgLibrary.web.repository;

import dm.dracolich.mtgLibrary.web.entity.GameFormatEntity;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface GameFormatRepository extends ReactiveMongoRepository<GameFormatEntity, String> {
    Mono<GameFormatEntity> findByCode(String code);
    Flux<GameFormatEntity> findByActiveTrue();
}
