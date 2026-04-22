package dm.dracolich.mtgLibrary.dto.records;

import com.fasterxml.jackson.annotation.JsonProperty;
import dm.dracolich.mtgLibrary.dto.enums.Color;
import dm.dracolich.mtgLibrary.dto.enums.Rarity;

import java.util.Set;

// Lightweight card projection for search results. Assembled by the service from
// a CardEntity plus one chosen ArtPropertyEntity (cheapest printing by default).
public record CardResumedRecord(
        String id,
        @JsonProperty("oracle_id") String oracleId,
        String name,
        @JsonProperty("full_type") String fullType,
        @JsonProperty("mana_cost") String manaCost,
        @JsonProperty("mana_value") Double manaValue,
        Set<Color> colors,
        @JsonProperty("color_identity") Set<Color> colorIdentity,
        Rarity rarity,
        @JsonProperty("set_code") String setCode,
        @JsonProperty("image_uri") String imageUri
) {}
