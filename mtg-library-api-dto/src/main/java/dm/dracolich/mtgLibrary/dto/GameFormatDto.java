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
public class GameFormatDto {
    private String id;
    private String code;
    private String name;
    private String description;
    @JsonProperty("min_deck_size")
    private Integer minDeckSize;
    @JsonProperty("max_deck_size")
    private Integer maxDeckSize;
    @JsonProperty("sideboard_size")
    private Integer sideboardSize;
    private Boolean singleton;
    @JsonProperty("uses_color_identity")
    private Boolean usesColorIdentity;
    @JsonProperty("commander_count")
    private Integer commanderCount;
    @JsonProperty("starting_life")
    private Integer startingLife;
    @JsonProperty("max_copies")
    private Integer maxCopies;
    private Boolean digital;
    private Boolean active;
}
