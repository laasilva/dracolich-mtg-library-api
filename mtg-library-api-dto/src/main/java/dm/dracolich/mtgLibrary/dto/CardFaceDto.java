package dm.dracolich.mtgLibrary.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import dm.dracolich.mtgLibrary.dto.enums.CardType;
import dm.dracolich.mtgLibrary.dto.enums.Supertype;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CardFaceDto {
    private String name;
    @JsonProperty("full_type")
    private String fullType;
    private Set<CardType> types;
    private Set<Supertype> supertypes;
    private Set<String> subtypes;
    @JsonProperty("oracle_text")
    private String oracleText;
    @JsonProperty("flavor_text")
    private String flavorText;
    @JsonProperty("gameplay_property")
    private GameplayPropertyDto gameplayProperty;
}
