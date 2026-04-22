package dm.dracolich.mtgLibrary.web.service;

import dm.dracolich.mtgLibrary.dto.CardLayoutDto;
import dm.dracolich.mtgLibrary.web.mapper.CardLayoutMapper;
import dm.dracolich.mtgLibrary.web.repository.CardLayoutRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import static dm.dracolich.mtgLibrary.web.helpers.ErrorUtil.notFound;

@Service
@RequiredArgsConstructor
public class CardLayoutServiceImpl implements CardLayoutService {
    private final CardLayoutRepository repo;
    private final CardLayoutMapper mapper;

    @Override
    public Mono<CardLayoutDto> fetchLayoutById(String id) {
        return repo.findById(id)
                .switchIfEmpty(Mono.error(notFound("layout", "id", id)))
                .map(mapper::entityToDto);
    }

    @Override
    public Mono<CardLayoutDto> fetchLayoutByCode(String code) {
        return repo.findByCode(code)
                .switchIfEmpty(Mono.error(notFound("layout", "code", code)))
                .map(mapper::entityToDto);
    }
}
