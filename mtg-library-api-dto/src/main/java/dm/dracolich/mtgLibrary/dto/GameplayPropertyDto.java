package dm.dracolich.mtgLibrary.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import dm.dracolich.mtgLibrary.dto.enums.Color;
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
public class GameplayPropertyDto {
    @JsonProperty("mana_cost")
    private String manaCost;
    @JsonProperty("mana_value")
    private Double manaValue;
    @JsonProperty("color_identity")
    private Set<Color> colorIdentity;
    private Set<Color> colors;
    private String power;
    private String toughness;
    private String defense;
    private String loyalty;
    @JsonProperty("life_modifier")
    private String lifeModifier;
    @JsonProperty("hand_modifier")
    private String handModifier;
    @JsonProperty("produced_mana")
    private Set<Color> producedMana;
    @JsonProperty("game_changer")
    private Boolean gameChanger;
}
