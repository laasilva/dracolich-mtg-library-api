package dm.dracolich.mtgLibrary.web.service;

import dm.dracolich.mtgLibrary.dto.GameFormatDto;
import reactor.core.publisher.Flux;

public interface GameFormatService {
    Flux<GameFormatDto> fetchAllFormats();
}
