package dm.dracolich.mtgLibrary.web.service;

import dm.dracolich.mtgLibrary.dto.CollectionSetDto;
import dm.dracolich.mtgLibrary.dto.records.SetSearchRecord;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import reactor.core.publisher.Mono;

public interface CollectionSetService {
    Mono<Page<CollectionSetDto>> searchSets(String name, SetSearchRecord filters, int page, int size);
    Mono<CollectionSetDto> fetchSetById(@PathVariable String id);
    Mono<CollectionSetDto> fetchSetByCode(@RequestParam String code);
}
