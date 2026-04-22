package dm.dracolich.mtgLibrary.web.seed;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import dm.dracolich.mtgLibrary.web.entity.GameFormatEntity;
import dm.dracolich.mtgLibrary.web.repository.GameFormatRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class GameFormatSeeder {

    private final GameFormatRepository gameFormatRepository;
    private final ReactiveMongoTemplate mongoTemplate;

    private static final ObjectMapper SEED_MAPPER = new ObjectMapper()
            .setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);

    @EventListener(ApplicationReadyEvent.class)
    public void seed() {
        gameFormatRepository.count()
                .flatMap(count -> {
                    if (count > 0) {
                        log.info("Game formats already seeded ({} formats). Skipping.", count);
                        return Mono.empty();
                    }
                    log.info("Seeding game formats from JSON...");
                    return loadAndPersist();
                })
                .subscribe(
                        unused -> {},
                        error -> log.error("Game format seed failed", error),
                        () -> log.info("Game format seed check complete.")
                );
    }

    public Mono<Void> syncFormats() {
        log.info("Syncing game formats from JSON...");
        return loadAndPersist();
    }

    private Mono<Void> loadAndPersist() {
        List<GameFormatEntity> formats;
        try {
            var resource = new ClassPathResource("seed/game-formats.json");
            formats = SEED_MAPPER.readValue(resource.getInputStream(), new TypeReference<>() {});
        } catch (IOException e) {
            return Mono.error(new IllegalStateException("Failed to read game-formats.json", e));
        }

        return Flux.fromIterable(formats)
                .concatMap(this::upsertFormat)
                .count()
                .doOnNext(count -> log.info("Game formats synced: {}.", count))
                .then();
    }

    private Mono<GameFormatEntity> upsertFormat(GameFormatEntity entity) {
        return mongoTemplate.findOne(
                        Query.query(Criteria.where("code").is(entity.getCode())),
                        GameFormatEntity.class)
                .flatMap(existing -> {
                    entity.setId(existing.getId());
                    return mongoTemplate.save(entity);
                })
                .switchIfEmpty(Mono.defer(() -> mongoTemplate.save(entity)));
    }
}
