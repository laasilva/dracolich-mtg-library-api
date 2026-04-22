package dm.dracolich.mtgLibrary.integration.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Map;

// Card face object — present in card_faces array for multi-faced cards.
// TODO: Fill in remaining fields from Scryfall docs.
@JsonIgnoreProperties(ignoreUnknown = true)
public record ScryfallCardFaceDto(
        String name,
        @JsonProperty("mana_cost") String manaCost,
        @JsonProperty("type_line") String typeLine,
        @JsonProperty("oracle_text") String oracleText,
        @JsonProperty("flavor_text") String flavorText,
        List<String> colors,
        @JsonProperty("color_indicator") List<String> colorIndicator,
        String power,
        String toughness,
        String loyalty,
        String defense,
        @JsonProperty("image_uris") Map<String, String> imageUris,
        String artist,
        @JsonProperty("artist_id") String artistId,
        @JsonProperty("illustration_id") String illustrationId
) {}
