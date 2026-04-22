package dm.dracolich.mtgLibrary.web.service;

import dm.dracolich.mtgLibrary.dto.ArtPropertyDto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ArtPropertyService {
    Mono<ArtPropertyDto> fetchArtPropertyById(String id);
    Flux<ArtPropertyDto> fetchArtPropertiesByCardId(String cardId);
}
