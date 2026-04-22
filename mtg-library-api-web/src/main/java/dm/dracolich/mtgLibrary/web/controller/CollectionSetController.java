package dm.dracolich.mtgLibrary.web.controller;

import dm.dracolich.mtgLibrary.dto.CollectionSetDto;
import dm.dracolich.mtgLibrary.dto.records.SetSearchRecord;
import dm.dracolich.mtgLibrary.web.service.CollectionSetService;
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
@RequestMapping("sets")
@Tag(name = "Sets")
@RequiredArgsConstructor
public class CollectionSetController {
    private final CollectionSetService service;

    @Operation(summary = "Search all sets", description = "Returns sets by filters")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Sets fetched successfully",
                    content = @Content(schema = @Schema(implementation = CollectionSetDto.class)))
    })
    @PostMapping(path = {"/search"}, produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<Page<CollectionSetDto>> searchSets(@RequestParam(required = false) String name,
                                                    @RequestBody SetSearchRecord filters,
                                                    @RequestParam int page,
                                                    @RequestParam int size) {
        return service.searchSets(name, filters, page, size);
    }

    @Operation(summary = "Fetch set by id", description = "Returns set by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Set fetched successfully",
                    content = @Content(schema = @Schema(implementation = CollectionSetDto.class)))
    })
    @GetMapping(path = {"/{id}"}, produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<CollectionSetDto> fetchSetById(@PathVariable String id) {
        return service.fetchSetById(id);
    }

    @Operation(summary = "Fetch set by code", description = "Returns set by code")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Set fetched successfully",
                    content = @Content(schema = @Schema(implementation = CollectionSetDto.class)))
    })
    @GetMapping(path = {""}, produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<CollectionSetDto> fetchSetByCode(@RequestParam String code) {
        return service.fetchSetByCode(code);
    }
}
