package dm.dracolich.mtgLibrary.dto.records;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import dm.dracolich.mtgLibrary.dto.enums.CardType;
import dm.dracolich.mtgLibrary.dto.enums.Color;
import dm.dracolich.mtgLibrary.dto.enums.Format;
import dm.dracolich.mtgLibrary.dto.enums.Rarity;

import java.util.Set;

// All filters optional. Empty record returns all cards, paginated.
public record CardSearchRecord(
        @JsonProperty("oracle_text") String oracleText,
        Set<Color> colors,
        @JsonProperty("color_identity") Set<Color> colorIdentity,
        Set<CardType> types,
        Set<String> subtypes,
        Set<String> keywords,
        @JsonProperty("legal_in") Format legalIn,
        Rarity rarity,
        @JsonProperty("min_mana_value") Double minManaValue,
        @JsonProperty("max_mana_value") Double maxManaValue,
        @JsonProperty("set_code") String setCode
) {}
