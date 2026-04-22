package dm.dracolich.mtgLibrary.integration.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class ScryfallConfig {

    @Value("${scryfall.api.base-url:https://api.scryfall.com}")
    private String baseUrl;

    @Bean
    public WebClient scryfallWebClient() {
        return WebClient.builder()
                .baseUrl(baseUrl)
                .defaultHeader("Accept", "application/json")
                .defaultHeader("User-Agent", "Dracolich/1.0")
                .codecs(configurer -> configurer.defaultCodecs()
                        .maxInMemorySize(4 * 1024 * 1024))
                .build();
    }
}
