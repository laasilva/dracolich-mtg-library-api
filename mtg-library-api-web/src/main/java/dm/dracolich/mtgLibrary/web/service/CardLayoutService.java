package dm.dracolich.mtgLibrary.web.service;

import dm.dracolich.mtgLibrary.dto.CardLayoutDto;
import reactor.core.publisher.Mono;

public interface CardLayoutService {
    Mono<CardLayoutDto> fetchLayoutById(String id);
    Mono<CardLayoutDto> fetchLayoutByCode(String code);
}
