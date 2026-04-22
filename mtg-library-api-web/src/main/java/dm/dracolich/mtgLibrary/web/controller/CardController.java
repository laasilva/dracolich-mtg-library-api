package dm.dracolich.mtgLibrary.web.controller;

import dm.dracolich.mtgLibrary.dto.CardDto;
import dm.dracolich.mtgLibrary.dto.records.CardSearchRecord;
import dm.dracolich.mtgLibrary.web.service.CardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("cards")
@Tag(name = "Cards")
@RequiredArgsConstructor
public class CardController {
    private final CardService service;

    @Operation(summary = "Search all cards", description = "Returns cards by name, filters")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cards fetched successfully",
                    content = @Content(schema = @Schema(implementation = CardDto.class)))
    })
    @PostMapping(path = {"/search"}, produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<Page<CardDto>> searchCards(@RequestParam(required = false) String name,
                                           @RequestBody CardSearchRecord filters,
                                           @RequestParam int page,
                                           @RequestParam int size) {
        return service.searchCard(name, filters, page, size);
    }

    @Operation(summary = "Search all cards", description = "Returns cards by name, filters")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cards fetched successfully",
                    content = @Content(schema = @Schema(implementation = CardDto.class)))
    })
    @PostMapping(path = {"/random"}, produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<CardDto> random(@RequestBody CardSearchRecord filters) {
        return service.fetchRandomCard(filters);
    }

    @Operation(summary = "Fetch card by id", description = "Returns card by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Card fetched successfully",
                    content = @Content(schema = @Schema(implementation = CardDto.class)))
    })
    @GetMapping(path = {"/{id}"}, produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<CardDto> fetchCardById(@PathVariable String id) {
        return service.fetchCardById(id);
    }

    @Operation(summary = "Fetch card by id", description = "Returns card by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Card fetched successfully",
                    content = @Content(schema = @Schema(implementation = CardDto.class)))
    })
    @GetMapping(path = {""}, produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<CardDto> fetchCardByName(@RequestParam String name) {
        return service.fetchCardByName(name);
    }
}
