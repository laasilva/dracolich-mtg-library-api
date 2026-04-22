package dm.dracolich.mtgLibrary.web.controller;

import dm.dracolich.mtgLibrary.dto.ArtPropertyDto;
import dm.dracolich.mtgLibrary.web.service.ArtPropertyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("art-properties")
@Tag(name = "Art Properties")
@RequiredArgsConstructor
public class ArtPropertyController {
    private final ArtPropertyService service;

    @Operation(summary = "Fetch art property by id", description = "Returns art property by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Art property fetched successfully",
                    content = @Content(schema = @Schema(implementation = ArtPropertyDto.class)))
    })
    @GetMapping(path = {"/{id}"}, produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ArtPropertyDto> fetchArtPropertyById(@PathVariable String id) {
        return service.fetchArtPropertyById(id);
    }

    @Operation(summary = "Fetch art properties by card id", description = "Returns all printings/art for a card")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Art properties fetched successfully",
                    content = @Content(schema = @Schema(implementation = ArtPropertyDto.class)))
    })
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public Flux<ArtPropertyDto> fetchArtPropertiesByCardId(@RequestParam("card_id") String cardId) {
        return service.fetchArtPropertiesByCardId(cardId);
    }
}
