package dm.dracolich.mtgLibrary.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SymbolDto {
    private String id;
    private String symbol;
    private String plaintext;
    private String alt;
    @JsonProperty("represents_mana")
    private Boolean representsMana;
    @JsonProperty("mana_value")
    private Integer manaValue;
    @JsonProperty("svg_uri")
    private String svgUri;
}
