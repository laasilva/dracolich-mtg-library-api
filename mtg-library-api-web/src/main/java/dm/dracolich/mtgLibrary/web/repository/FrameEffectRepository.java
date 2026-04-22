package dm.dracolich.mtgLibrary.web.repository;

import dm.dracolich.mtgLibrary.web.entity.FrameEffectEntity;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

public interface FrameEffectRepository extends ReactiveMongoRepository<FrameEffectEntity, String> {
    Mono<FrameEffectEntity> findByCode(String code);
}
