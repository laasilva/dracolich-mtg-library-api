package dm.dracolich.mtgLibrary.web.controller;

import dm.dracolich.mtgLibrary.dto.GameFormatDto;
import dm.dracolich.mtgLibrary.dto.RulingDto;
import dm.dracolich.mtgLibrary.web.service.GameFormatService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("formats")
@Tag(name = "Formats")
@RequiredArgsConstructor
public class GameFormatController {
    private final GameFormatService service;

    @Operation(summary = "Fetch all game formats", description = "Returns all game formats")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Game formats fetched successfully",
                    content = @Content(schema = @Schema(implementation = GameFormatDto.class)))
    })
    @GetMapping(path = {"/"}, produces = MediaType.APPLICATION_JSON_VALUE)
    public Flux<GameFormatDto> fetchRulingById() {
        return service.fetchAllFormats();
    }
}
