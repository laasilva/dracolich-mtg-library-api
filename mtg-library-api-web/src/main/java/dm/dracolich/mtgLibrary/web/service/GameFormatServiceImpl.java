package dm.dracolich.mtgLibrary.web.service;

import dm.dracolich.mtgLibrary.dto.GameFormatDto;
import dm.dracolich.mtgLibrary.web.mapper.GameFormatMapper;
import dm.dracolich.mtgLibrary.web.repository.GameFormatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
@RequiredArgsConstructor
public class GameFormatServiceImpl implements GameFormatService {
    private final GameFormatRepository repository;
    private final GameFormatMapper mapper;

    @Override
    public Flux<GameFormatDto> fetchAllFormats() {
        return repository.findAll()
                .map(mapper::entityToDto);
    }
}
