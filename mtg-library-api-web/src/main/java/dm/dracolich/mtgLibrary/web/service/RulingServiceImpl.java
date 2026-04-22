package dm.dracolich.mtgLibrary.web.service;

import dm.dracolich.mtgLibrary.dto.RulingDto;
import dm.dracolich.mtgLibrary.web.mapper.RulingMapper;
import dm.dracolich.mtgLibrary.web.repository.RulingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import static dm.dracolich.mtgLibrary.web.helpers.ErrorUtil.notFound;

@Service
@RequiredArgsConstructor
public class RulingServiceImpl implements RulingService {
    private final RulingRepository repo;
    private final RulingMapper mapper;

    @Override
    public Mono<RulingDto> fetchRulingById(String id) {
        return repo.findById(id)
                .switchIfEmpty(Mono.error(notFound("ruling", "id", id)))
            .map(mapper::entityToDto);
    }
}
