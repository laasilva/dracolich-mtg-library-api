package dm.dracolich.mtgLibrary.integration.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

// One entry from GET /bulk-data — describes a downloadable file.
@JsonIgnoreProperties(ignoreUnknown = true)
public record ScryfallBulkDataDto(
        String id,
        String type,
        String name,
        String description,
        @JsonProperty("download_uri") String downloadUri,
        @JsonProperty("updated_at") String updatedAt,
        @JsonProperty("size") Long size,
        @JsonProperty("content_type") String contentType,
        @JsonProperty("content_encoding") String contentEncoding
) {}
