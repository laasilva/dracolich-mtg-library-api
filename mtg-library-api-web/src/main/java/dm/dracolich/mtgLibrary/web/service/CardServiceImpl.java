package dm.dracolich.mtgLibrary.web.service;

import dm.dracolich.forge.error.ApiError;
import dm.dracolich.forge.exception.ResponseException;
import dm.dracolich.mtgLibrary.dto.CardDto;
import dm.dracolich.mtgLibrary.dto.error.ErrorCodes;
import dm.dracolich.mtgLibrary.dto.records.CardSearchRecord;
import dm.dracolich.mtgLibrary.web.mapper.CardMapper;
import dm.dracolich.mtgLibrary.web.repository.CardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

import static dm.dracolich.mtgLibrary.web.helpers.ErrorUtil.notFound;

@Service
@RequiredArgsConstructor
public class CardServiceImpl implements CardService {
    private final CardRepository repo;
    private final CardMapper mapper;

    @Override
    public Mono<Page<CardDto>> searchCard(String name, CardSearchRecord cardSearchRecord,  int page, int size) {
        return repo.findByFilters(name, cardSearchRecord, page, size)
                .map(c -> c.map(mapper::entityToDto));
    }

    @Override
    public Mono<CardDto> fetchRandomCard(CardSearchRecord cardSearchRecord) {
        return repo.findRandom(cardSearchRecord)
                .map(mapper::entityToDto);
    }

    @Override
    public Mono<CardDto> fetchCardById(String id) {
        return repo.findById(id).map(mapper::entityToDto);
    }

    @Override
    public Mono<CardDto> fetchCardByName(String name) {
        return repo.findByNameIgnoreCase(name)
                .switchIfEmpty(Mono.error(notFound("card", "name", name)))
            .map(mapper::entityToDto);
    }
}
