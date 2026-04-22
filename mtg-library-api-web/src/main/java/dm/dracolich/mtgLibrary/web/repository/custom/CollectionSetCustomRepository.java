package dm.dracolich.mtgLibrary.web.repository.custom;

import dm.dracolich.mtgLibrary.dto.records.SetSearchRecord;
import dm.dracolich.mtgLibrary.web.entity.CollectionSetEntity;
import org.springframework.data.domain.Page;
import reactor.core.publisher.Mono;

public interface CollectionSetCustomRepository {
    Mono<Page<CollectionSetEntity>> findByFilters(String name, SetSearchRecord setSearchRecord, int page, int size);
}
