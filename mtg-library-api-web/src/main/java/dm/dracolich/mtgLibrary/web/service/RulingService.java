package dm.dracolich.mtgLibrary.web.service;

import dm.dracolich.mtgLibrary.dto.RulingDto;
import reactor.core.publisher.Mono;

public interface RulingService {
    Mono<RulingDto> fetchRulingById(String id);
}
