package dm.dracolich.mtgLibrary.web.entity;

import dm.dracolich.mtgLibrary.dto.enums.CardType;
import dm.dracolich.mtgLibrary.dto.enums.Supertype;
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
public class CardFaceEntity {
    private String name;
    @Field("full_type")
    private String fullType;
    private Set<CardType> types;
    private Set<Supertype> supertypes;
    // References to SubtypeEntity.id. Each subtype id encodes its category,
    // e.g. "enchantment-saga", "artifact-equipment", "creature-goblin".
    private Set<String> subtypes;
    @Field("oracle_text")
    private String oracleText;
    @Field("flavor_text")
    private String flavorText;
    @Field("gameplay_property")
    private GameplayPropertyEntity gameplayProperty;
}
