package dm.dracolich.mtgLibrary.web.service;

import dm.dracolich.mtgLibrary.dto.CardDto;
import dm.dracolich.mtgLibrary.dto.records.CardSearchRecord;
import org.springframework.data.domain.Page;
import reactor.core.publisher.Mono;

public interface CardService {
    Mono<Page<CardDto>> searchCard(String name, CardSearchRecord cardSearchRecord, int page, int size);
    Mono<CardDto> fetchRandomCard(CardSearchRecord cardSearchRecord);
    Mono<CardDto> fetchCardById(String id);
    Mono<CardDto> fetchCardByName(String name);
}
