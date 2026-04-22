package dm.dracolich.mtgLibrary.web.controller;

import dm.dracolich.mtgLibrary.dto.CardDto;
import dm.dracolich.mtgLibrary.dto.CollectionSetDto;
import dm.dracolich.mtgLibrary.dto.RulingDto;
import dm.dracolich.mtgLibrary.web.service.RulingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("rulings")
@Tag(name = "Rulings")
@RequiredArgsConstructor
public class RulingController {
    private final RulingService service;

    @Operation(summary = "Fetch ruling by id", description = "Returns ruling by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ruling fetched successfully",
                    content = @Content(schema = @Schema(implementation = RulingDto.class)))
    })
    @GetMapping(path = {"/{id}"}, produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<RulingDto> fetchRulingById(@PathVariable String id) {
        return service.fetchRulingById(id);
    }
}
