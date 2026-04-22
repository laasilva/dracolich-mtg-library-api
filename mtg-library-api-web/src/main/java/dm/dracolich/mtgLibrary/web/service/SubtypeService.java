package dm.dracolich.mtgLibrary.web.service;

import dm.dracolich.mtgLibrary.dto.SubtypeDto;
import org.springframework.data.domain.Page;
import reactor.core.publisher.Mono;

public interface SubtypeService {
    Mono<SubtypeDto> fetchSubtypeById(String id);
    Mono<SubtypeDto> fetchSubtypeByCode(String code);
    Mono<Page<SubtypeDto>> fetchAll(int page, int size);
}
