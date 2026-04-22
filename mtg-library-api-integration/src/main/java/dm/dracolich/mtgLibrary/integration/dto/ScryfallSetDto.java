package dm.dracolich.mtgLibrary.integration.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

// Set object from GET /sets.
// TODO: Fill in remaining fields from Scryfall docs.
@JsonIgnoreProperties(ignoreUnknown = true)
public record ScryfallSetDto(
        String id,
        String code,
        String name,
        @JsonProperty("set_type") String setType,
        @JsonProperty("released_at") String releasedAt,
        @JsonProperty("block_code") String blockCode,
        String block,
        @JsonProperty("parent_set_code") String parentSetCode,
        @JsonProperty("card_count") Integer cardCount,
        Boolean digital,
        @JsonProperty("foil_only") Boolean foilOnly,
        @JsonProperty("non_foil_only") Boolean nonFoilOnly,
        @JsonProperty("icon_svg_uri") String iconSvgUri
) {}
