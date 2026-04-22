package dm.dracolich.mtgLibrary.web.controller;

import dm.dracolich.mtgLibrary.dto.SubtypeDto;
import dm.dracolich.mtgLibrary.web.service.SubtypeService;
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
@RequestMapping("subtypes")
@Tag(name = "Subtypes")
@RequiredArgsConstructor
public class SubtypeController {
    private final SubtypeService service;

    @Operation(summary = "Fetch all subtypes", description = "Returns all subtypes, paginated")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Subtypes fetched successfully",
                    content = @Content(schema = @Schema(implementation = SubtypeDto.class)))
    })
    @GetMapping(path = {"/search"}, produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<Page<SubtypeDto>> fetchAll(@RequestParam int page, @RequestParam int size) {
        return service.fetchAll(page, size);
    }

    @Operation(summary = "Fetch subtype by id", description = "Returns subtype by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Subtype fetched successfully",
                    content = @Content(schema = @Schema(implementation = SubtypeDto.class)))
    })
    @GetMapping(path = {"/{id}"}, produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<SubtypeDto> fetchSubtypeById(@PathVariable String id) {
        return service.fetchSubtypeById(id);
    }

    @Operation(summary = "Fetch subtype by code", description = "Returns subtype by code (e.g. creature-goblin)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Subtype fetched successfully",
                    content = @Content(schema = @Schema(implementation = SubtypeDto.class)))
    })
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<SubtypeDto> fetchSubtypeByCode(@RequestParam String code) {
        return service.fetchSubtypeByCode(code);
    }
}
