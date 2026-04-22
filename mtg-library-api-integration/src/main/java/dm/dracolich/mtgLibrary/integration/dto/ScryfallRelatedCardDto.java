package dm.dracolich.mtgLibrary.integration.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

// Entry in the all_parts array — tokens, meld pairs, etc.
@JsonIgnoreProperties(ignoreUnknown = true)
public record ScryfallRelatedCardDto(
        String id,
        String component,
        String name,
        @JsonProperty("type_line") String typeLine
) {}
