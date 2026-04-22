package dm.dracolich.mtgLibrary.web.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "symbols")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SymbolEntity {
    @Id
    private String id;
    @Indexed(unique = true)
    private String symbol;
    private String plaintext;
    private String alt;
    @Field("represents_mana")
    private Boolean representsMana;
    @Field("mana_value")
    private Integer manaValue;
    @Field("svg_uri")
    private String svgUri;
}
