package dm.dracolich.mtgLibrary.integration.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

// Scryfall wraps all list responses in this envelope.
@JsonIgnoreProperties(ignoreUnknown = true)
public record ScryfallListDto<T>(
        String object,
        @JsonProperty("has_more") Boolean hasMore,
        @JsonProperty("next_page") String nextPage,
        @JsonProperty("total_cards") Integer totalCards,
        List<T> data
) {}
