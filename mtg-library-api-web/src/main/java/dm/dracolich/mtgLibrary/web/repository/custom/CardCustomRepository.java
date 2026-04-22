package dm.dracolich.mtgLibrary.web.repository.custom;

import dm.dracolich.mtgLibrary.dto.records.CardSearchRecord;
import dm.dracolich.mtgLibrary.web.entity.CardEntity;
import org.springframework.data.domain.Page;
import reactor.core.publisher.Mono;

public interface CardCustomRepository {
    Mono<Page<CardEntity>> findByFilters(String name, CardSearchRecord cardSearchRecord,  int page, int size);
    Mono<CardEntity> findRandom(CardSearchRecord cardSearchRecord);
}
