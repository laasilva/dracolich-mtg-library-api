package dm.dracolich.mtgLibrary.web.controller;

import dm.dracolich.mtgLibrary.dto.SymbolDto;
import dm.dracolich.mtgLibrary.web.service.SymbolService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("symbols")
@Tag(name = "Symbols")
@RequiredArgsConstructor
public class SymbolController {
    private final SymbolService service;

    @Operation(summary = "Fetch symbol by id", description = "Returns symbol by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Symbol fetched successfully",
                    content = @Content(schema = @Schema(implementation = SymbolDto.class)))
    })
    @GetMapping(path = {"/{id}"}, produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<SymbolDto> fetchSymbolById(@PathVariable String id) {
        return service.fetchSymbolById(id);
    }

    @Operation(summary = "Fetch symbol by symbol notation", description = "Returns symbol by its notation (e.g. {W}, {U})")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Symbol fetched successfully",
                    content = @Content(schema = @Schema(implementation = SymbolDto.class)))
    })
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<SymbolDto> fetchSymbolBySymbol(@RequestParam String symbol) {
        return service.fetchSymbolBySymbol(symbol);
    }
}
