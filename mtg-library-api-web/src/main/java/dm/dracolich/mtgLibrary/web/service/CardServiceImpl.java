package dm.dracolich.mtgLibrary.web.service;

import dm.dracolich.mtgLibrary.dto.ArtPropertyDto;
import dm.dracolich.mtgLibrary.dto.CardDto;
import dm.dracolich.mtgLibrary.dto.records.CardSearchRecord;
import dm.dracolich.mtgLibrary.web.entity.ArtPropertyEntity;
import dm.dracolich.mtgLibrary.web.entity.CardEntity;
import dm.dracolich.mtgLibrary.web.mapper.ArtPropertyMapper;
import dm.dracolich.mtgLibrary.web.mapper.CardMapper;
import dm.dracolich.mtgLibrary.web.repository.ArtPropertyRepository;
import dm.dracolich.mtgLibrary.web.repository.CardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Comparator;
import java.util.Map;

import static dm.dracolich.mtgLibrary.web.helpers.ErrorUtil.notFound;

@Service
@RequiredArgsConstructor
public class CardServiceImpl implements CardService {
    private final CardRepository repo;
    private final ArtPropertyRepository artRepo;
    private final CardMapper mapper;
    private final ArtPropertyMapper artMapper;

    @Override
    public Mono<Page<CardDto>> searchCard(String name, CardSearchRecord cardSearchRecord, int page, int size) {
        return repo.findByFilters(name, cardSearchRecord, page, size)
                .flatMap(p -> Flux.fromIterable(p.getContent())
                        .flatMap(this::withDefaultArt)
                        .collectList()
                        .map(content -> (Page<CardDto>) new PageImpl<>(content, p.getPageable(), p.getTotalElements())));
    }

    @Override
    public Mono<CardDto> fetchRandomCard(CardSearchRecord cardSearchRecord) {
        return repo.findRandom(cardSearchRecord)
                .flatMap(this::withDefaultArt);
    }

    @Override
    public Mono<CardDto> fetchCardById(String id) {
        return repo.findById(id)
                .switchIfEmpty(Mono.error(notFound("card", "id", id)))
                .flatMap(this::withDefaultArt);
    }

    @Override
    public Mono<CardDto> fetchCardByName(String name) {
        return repo.findByNameIgnoreCase(name)
                .switchIfEmpty(Mono.error(notFound("card", "name", name)))
                .flatMap(this::withDefaultArt);
    }

    private Mono<CardDto> withDefaultArt(CardEntity card) {
        return artRepo.findByCardId(card.getId())
                .collectList()
                .map(arts -> {
                    if (arts.isEmpty()) {
                        return mapper.entityToDto(card);
                    }
                    ArtPropertyEntity chosen = arts.stream()
                            .min(Comparator.comparing(CardServiceImpl::cheapestPrice,
                                    Comparator.nullsLast(Double::compareTo)))
                            .orElse(arts.getFirst());
                    ArtPropertyDto chosenDto = artMapper.entityToDto(chosen);
                    return mapper.entityToDtoWithArt(card, chosenDto);
                });
    }

    private static Double cheapestPrice(ArtPropertyEntity art) {
        Map<String, Double> prices = art.getPrices();
        if (prices == null || prices.isEmpty()) {
            return null;
        }
        return prices.values().stream()
                .filter(v -> v != null && v > 0)
                .min(Double::compareTo)
                .orElse(null);
    }
}
