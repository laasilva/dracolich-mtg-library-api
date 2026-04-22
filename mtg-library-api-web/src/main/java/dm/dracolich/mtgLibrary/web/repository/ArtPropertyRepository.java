package dm.dracolich.mtgLibrary.web.repository;

import dm.dracolich.mtgLibrary.web.entity.ArtPropertyEntity;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ArtPropertyRepository extends ReactiveMongoRepository<ArtPropertyEntity, String> {
    Flux<ArtPropertyEntity> findByCardId(String cardId);
    Mono<ArtPropertyEntity> findByScryfallId(String scryfallId);
}
