package dm.dracolich.mtgLibrary.web.controller;

import dm.dracolich.mtgLibrary.dto.CardLayoutDto;
import dm.dracolich.mtgLibrary.web.service.CardLayoutService;
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
@RequestMapping("layouts")
@Tag(name = "Card Layouts")
@RequiredArgsConstructor
public class CardLayoutController {
    private final CardLayoutService service;

    @Operation(summary = "Fetch layout by id", description = "Returns card layout by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Layout fetched successfully",
                    content = @Content(schema = @Schema(implementation = CardLayoutDto.class)))
    })
    @GetMapping(path = {"/{id}"}, produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<CardLayoutDto> fetchLayoutById(@PathVariable String id) {
        return service.fetchLayoutById(id);
    }

    @Operation(summary = "Fetch layout by code", description = "Returns card layout by code (e.g. normal, transform)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Layout fetched successfully",
                    content = @Content(schema = @Schema(implementation = CardLayoutDto.class)))
    })
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<CardLayoutDto> fetchLayoutByCode(@RequestParam String code) {
        return service.fetchLayoutByCode(code);
    }
}
