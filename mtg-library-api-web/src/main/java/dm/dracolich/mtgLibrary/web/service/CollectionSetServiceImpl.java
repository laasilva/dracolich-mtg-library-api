package dm.dracolich.mtgLibrary.web.service;

import dm.dracolich.mtgLibrary.dto.CollectionSetDto;
import dm.dracolich.mtgLibrary.dto.records.SetSearchRecord;
import dm.dracolich.mtgLibrary.web.mapper.CollectionSetMapper;
import dm.dracolich.mtgLibrary.web.repository.CollectionSetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import static dm.dracolich.mtgLibrary.web.helpers.ErrorUtil.notFound;

@Service
@RequiredArgsConstructor
public class CollectionSetServiceImpl implements CollectionSetService {
    private final CollectionSetRepository repo;
    private final CollectionSetMapper mapper;

    @Override
    public Mono<Page<CollectionSetDto>> searchSets(String name, SetSearchRecord filters, int page, int size) {
        return repo.findByFilters(name, filters, page, size)
                .map(entityPage -> new PageImpl<>(
                        entityPage.getContent().stream().map(mapper::entityToDto).toList(),
                        entityPage.getPageable(),
                        entityPage.getTotalElements()));
    }

    @Override
    public Mono<CollectionSetDto> fetchSetById(String id) {
        return repo.findById(id)
                .switchIfEmpty(Mono.error(notFound("set", "id", id)))
            .map(mapper::entityToDto);
    }

    @Override
    public Mono<CollectionSetDto> fetchSetByCode(String code) {
        return repo.findByCode(code)
                .switchIfEmpty(Mono.error(notFound("set", "code", code)))
            .map(mapper::entityToDto);
    }
}
