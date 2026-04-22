package dm.dracolich.mtgLibrary.web.controller;

import dm.dracolich.mtgLibrary.web.seed.ScryfallSeeder;
import dm.dracolich.forge.response.DmdResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("sync")
@RequiredArgsConstructor
public class SyncController {

    private final ScryfallSeeder seeder;

    @PostMapping
    public Mono<DmdResponse<String>> sync() {
        return seeder.sync()
                .thenReturn(DmdResponse.of("Scryfall sync complete.", HttpStatus.OK, "Scryfall sync complete."))
                .onErrorResume(IllegalStateException.class, e ->
                        Mono.just(DmdResponse.of((String) null, HttpStatus.CONFLICT, e.getMessage())));
    }
}
