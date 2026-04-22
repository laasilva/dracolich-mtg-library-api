package dm.dracolich.mtgLibrary.web.service;

import dm.dracolich.mtgLibrary.dto.ArtPropertyDto;
import dm.dracolich.mtgLibrary.web.mapper.ArtPropertyMapper;
import dm.dracolich.mtgLibrary.web.repository.ArtPropertyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static dm.dracolich.mtgLibrary.web.helpers.ErrorUtil.notFound;

@Service
@RequiredArgsConstructor
public class ArtPropertyServiceImpl implements ArtPropertyService {
    private final ArtPropertyRepository repo;
    private final ArtPropertyMapper mapper;

    @Override
    public Mono<ArtPropertyDto> fetchArtPropertyById(String id) {
        return repo.findById(id)
                .switchIfEmpty(Mono.error(notFound("art property", "id", id)))
                .map(mapper::entityToDto);
    }

    @Override
    public Flux<ArtPropertyDto> fetchArtPropertiesByCardId(String cardId) {
        return repo.findByCardId(cardId)
                .map(mapper::entityToDto);
    }
}
