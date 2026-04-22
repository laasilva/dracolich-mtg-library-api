package dm.dracolich.mtgLibrary.web.service;

import dm.dracolich.mtgLibrary.dto.SymbolDto;
import reactor.core.publisher.Mono;

public interface SymbolService {
    Mono<SymbolDto> fetchSymbolById(String id);
    Mono<SymbolDto> fetchSymbolBySymbol(String symbol);
}
