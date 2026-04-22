package dm.dracolich.mtgLibrary.web.repository;

import dm.dracolich.mtgLibrary.web.entity.SubtypeEntity;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface SubtypeRepository extends ReactiveMongoRepository<SubtypeEntity, String> {
    Flux<SubtypeEntity> findByCategory(String category);
    Mono<SubtypeEntity> findByCode(String code);
}
