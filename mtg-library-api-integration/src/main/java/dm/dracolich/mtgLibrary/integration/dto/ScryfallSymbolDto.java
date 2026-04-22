package dm.dracolich.mtgLibrary.integration.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

// Symbol object from GET /symbology.
@JsonIgnoreProperties(ignoreUnknown = true)
public record ScryfallSymbolDto(
        String symbol,
        @JsonProperty("svg_uri") String svgUri,
        @JsonProperty("loose_variant") String looseVariant,
        String english,
        Boolean transposable,
        @JsonProperty("represents_mana") Boolean representsMana,
        @JsonProperty("mana_value") Double manaValue,
        @JsonProperty("appears_in_mana_costs") Boolean appearsInManaCosts,
        Boolean funny,
        List<String> colors
) {}
