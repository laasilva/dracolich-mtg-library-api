package dm.dracolich.mtgLibrary.web.entity;

import dm.dracolich.mtgLibrary.dto.enums.Color;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GameplayPropertyEntity {
    @Field("mana_cost")
    private String manaCost;
    @Field("mana_value")
    private Double manaValue;
    @Field("color_identity")
    private Set<Color> colorIdentity;
    private Set<Color> colors;
    private String power;
    private String toughness;
    private String defense;
    private String loyalty;
    @Field("life_modifier")
    private String lifeModifier;
    @Field("hand_modifier")
    private String handModifier;
    @Field("produced_mana")
    private Set<Color> producedMana;
    @Field("game_changer")
    private Boolean gameChanger;
}
