package dm.dracolich.mtgLibrary.web.service;

import dm.dracolich.mtgLibrary.dto.SubtypeDto;
import dm.dracolich.mtgLibrary.web.mapper.SubtypeMapper;
import dm.dracolich.mtgLibrary.web.repository.SubtypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import static dm.dracolich.mtgLibrary.web.helpers.ErrorUtil.notFound;

@Service
@RequiredArgsConstructor
public class SubtypeServiceImpl implements SubtypeService {
    private final SubtypeRepository repo;
    private final SubtypeMapper mapper;

    @Override
    public Mono<SubtypeDto> fetchSubtypeById(String id) {
        return repo.findById(id)
                .switchIfEmpty(Mono.error(notFound("subtype", "id", id)))
                .map(mapper::entityToDto);
    }

    @Override
    public Mono<SubtypeDto> fetchSubtypeByCode(String code) {
        return repo.findByCode(code)
                .switchIfEmpty(Mono.error(notFound("subtype", "code", code)))
                .map(mapper::entityToDto);
    }

    @Override
    public Mono<Page<SubtypeDto>> fetchAll(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        return repo.findAll(pageRequest.getSort())
                .collectList()
                .zipWith(repo.count())
                .map(tuple -> {
                    var all = tuple.getT1();
                    long total = tuple.getT2();
                    int start = (int) pageRequest.getOffset();
                    int end = Math.min(start + size, all.size());
                    var pageContent = start < all.size() ? all.subList(start, end) : java.util.List.<dm.dracolich.mtgLibrary.web.entity.SubtypeEntity>of();
                    return (Page<SubtypeDto>) new PageImpl<>(
                            pageContent.stream().map(mapper::entityToDto).toList(),
                            pageRequest,
                            total);
                });
    }
}
