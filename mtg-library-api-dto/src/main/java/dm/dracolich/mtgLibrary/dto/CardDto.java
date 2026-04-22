package dm.dracolich.mtgLibrary.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import dm.dracolich.mtgLibrary.dto.enums.Format;
import dm.dracolich.mtgLibrary.dto.enums.Legality;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CardDto {
    private String id;
    @JsonProperty("oracle_id")
    private String oracleId;
    private String name;
    private Set<String> keywords;
    private Map<Format, Legality> legalities;
    private String layout;
    private Boolean reserved;
    @JsonProperty("first_release_date")
    private LocalDate firstReleaseDate;
    @JsonProperty("edhrec_rank")
    private Integer edhrecRank;
    @JsonProperty("penny_rank")
    private Integer pennyRank;
    private Boolean multiface;
    @JsonProperty("default_face")
    private CardFaceDto defaultFace;
    @JsonProperty("flipped_face")
    private CardFaceDto flippedFace;
    @JsonProperty("all_parts")
    private List<RelatedCardPartDto> allParts;
    // Populated service-side with the chosen printing (default: cheapest non-foil).
    @JsonProperty("default_art")
    private ArtPropertyDto defaultArt;
}
