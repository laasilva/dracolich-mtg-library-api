package dm.dracolich.mtgLibrary.web.service;

import dm.dracolich.mtgLibrary.dto.SymbolDto;
import dm.dracolich.mtgLibrary.web.mapper.SymbolMapper;
import dm.dracolich.mtgLibrary.web.repository.SymbolRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import static dm.dracolich.mtgLibrary.web.helpers.ErrorUtil.notFound;

@Service
@RequiredArgsConstructor
public class SymbolServiceImpl implements SymbolService {
    private final SymbolRepository repo;
    private final SymbolMapper mapper;

    @Override
    public Mono<SymbolDto> fetchSymbolById(String id) {
        return repo.findById(id)
                .switchIfEmpty(Mono.error(notFound("symbol", "id", id)))
                .map(mapper::entityToDto);
    }

    @Override
    public Mono<SymbolDto> fetchSymbolBySymbol(String symbol) {
        return repo.findBySymbol(symbol)
                .switchIfEmpty(Mono.error(notFound("symbol", "symbol", symbol)))
                .map(mapper::entityToDto);
    }
}
