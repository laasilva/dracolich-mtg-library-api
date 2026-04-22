package dm.dracolich.mtgLibrary.integration.client;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.ObjectMapper;
import dm.dracolich.mtgLibrary.integration.dto.ScryfallBulkDataDto;
import dm.dracolich.mtgLibrary.integration.dto.ScryfallListDto;
import dm.dracolich.mtgLibrary.integration.dto.ScryfallRulingDto;
import dm.dracolich.mtgLibrary.integration.dto.ScryfallCardDto;
import dm.dracolich.mtgLibrary.integration.dto.ScryfallSetDto;
import dm.dracolich.mtgLibrary.integration.dto.ScryfallSymbolDto;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.io.InputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;

@Component
public class ScryfallClient {

    private final WebClient webClient;
    private final ObjectMapper objectMapper;

    public ScryfallClient(WebClient scryfallWebClient) {
        this.webClient = scryfallWebClient;
        this.objectMapper = new ObjectMapper()
                .configure(com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    // GET /bulk-data — returns available bulk data downloads
    public Mono<ScryfallListDto<ScryfallBulkDataDto>> getBulkDataList() {
        return webClient.get()
                .uri("/bulk-data")
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<>() {});
    }

    // Streams cards one-by-one from the bulk data download using Jackson streaming.
    // The full JSON array is never loaded into memory.
    public Flux<ScryfallCardDto> streamBulkCards(String downloadUri) {
        return streamBulkData(downloadUri, ScryfallCardDto.class);
    }

    public Flux<ScryfallRulingDto> streamBulkRulings(String downloadUri) {
        return streamBulkData(downloadUri, ScryfallRulingDto.class);
    }

    // GET /sets — returns all sets
    public Mono<ScryfallListDto<ScryfallSetDto>> getSets() {
        return webClient.get()
                .uri("/sets")
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<>() {});
    }

    // GET /symbology — returns all mana symbols
    public Mono<ScryfallListDto<ScryfallSymbolDto>> getSymbology() {
        return webClient.get()
                .uri("/symbology")
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<>() {});
    }

    // Streams a bulk data JSON array from Scryfall's CDN. Uses Jackson's streaming
    // parser to read one object at a time, keeping memory usage flat regardless of
    // file size (~80MB for oracle_cards, ~300MB for all_cards).
    private <T> Flux<T> streamBulkData(String downloadUri, Class<T> type) {
        return Flux.create(sink -> {
            PipedInputStream pipedIn = new PipedInputStream(65536);

            // Pipe WebClient's DataBuffer stream into an InputStream for Jackson
            try {
                PipedOutputStream pipedOut = new PipedOutputStream(pipedIn);

                Flux<DataBuffer> dataStream = WebClient.create()
                        .get()
                        .uri(downloadUri)
                        .retrieve()
                        .bodyToFlux(DataBuffer.class);

                // Write DataBuffers into the pipe on a separate thread
                DataBufferUtils.write(dataStream, pipedOut)
                        .subscribe(
                                DataBufferUtils::release,
                                error -> {
                                    closeQuietly(pipedOut);
                                    sink.error(error);
                                },
                                () -> closeQuietly(pipedOut)
                        );

                // Parse the JSON array from the piped input stream
                parseJsonArray(pipedIn, type, sink);

            } catch (IOException e) {
                sink.error(e);
            }
        });
    }

    private <T> void parseJsonArray(InputStream inputStream, Class<T> type,
                                     reactor.core.publisher.FluxSink<T> sink) {
        Thread.ofVirtual().start(() -> {
            try (JsonParser parser = objectMapper.getFactory().createParser(inputStream)) {
                // Advance to the start of the array
                if (parser.nextToken() != JsonToken.START_ARRAY) {
                    sink.error(new IOException("Expected JSON array, got: " + parser.currentToken()));
                    return;
                }

                // Read each object in the array one at a time
                while (parser.nextToken() == JsonToken.START_OBJECT) {
                    T item = objectMapper.readValue(parser, type);
                    sink.next(item);
                }

                sink.complete();
            } catch (IOException e) {
                sink.error(e);
            }
        });
    }

    private void closeQuietly(PipedOutputStream stream) {
        try { stream.close(); } catch (IOException ignored) {}
    }
}
