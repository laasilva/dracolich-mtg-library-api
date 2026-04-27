package dm.dracolich.mtgLibrary.web.service;

import dm.dracolich.mtgLibrary.dto.GameFormatDto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface GameFormatService {
    Flux<GameFormatDto> fetchAllFormats();
    Mono<GameFormatDto> fetchFormatByCode(String name);
}
