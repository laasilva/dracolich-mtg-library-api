package dm.dracolich.mtgLibrary.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import dm.dracolich.mtgLibrary.dto.enums.CollectionSetType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CollectionSetDto {
    private String id;
    @JsonProperty("scryfall_id")
    private String scryfallId;
    private String name;
    private String code;
    @JsonProperty("parent_set_code")
    private String parentSetCode;
    private String block;
    @JsonProperty("collection_set_type")
    private CollectionSetType collectionSetType;
    @JsonProperty("release_date")
    private LocalDate releaseDate;
    @JsonProperty("icon_svg_uri")
    private String iconSvgUri;
    @JsonProperty("card_count")
    private Integer cardCount;
    private Boolean digital;
    @JsonProperty("foil_only")
    private Boolean foilOnly;
    @JsonProperty("non_foil_only")
    private Boolean nonFoilOnly;
}
