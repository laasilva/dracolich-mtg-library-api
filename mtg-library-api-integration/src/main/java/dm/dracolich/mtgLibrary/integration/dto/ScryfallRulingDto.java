package dm.dracolich.mtgLibrary.integration.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

// Ruling object from the rulings bulk data file.
@JsonIgnoreProperties(ignoreUnknown = true)
public record ScryfallRulingDto(
        @JsonProperty("oracle_id") String oracleId,
        String source,
        @JsonProperty("published_at") String publishedAt,
        String comment
) {}
