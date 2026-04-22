package dm.dracolich.mtgLibrary.web.entity;

import dm.dracolich.mtgLibrary.dto.enums.Format;
import dm.dracolich.mtgLibrary.dto.enums.Legality;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Document(collection = "cards")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CardEntity {
    @Id
    private String id;
    @Indexed(unique = true)
    @Field("oracle_id")
    private String oracleId;
    private String name;
    private Set<String> keywords;
    private Map<Format, Legality> legalities;
    private String layout; // Reference to CardLayoutEntity.id (e.g. "normal", "transform", "case").
    private Boolean reserved;
    @Field("first_release_date")
    private LocalDate firstReleaseDate;
    @Field("edhrec_rank")
    private Integer edhrecRank;
    @Field("penny_rank")
    private Integer pennyRank;
    private Boolean multiface;
    @Field("default_face")
    private CardFaceEntity defaultFace;
    @Field("flipped_face")
    private CardFaceEntity flippedFace;
    @Field("all_parts")
    private List<RelatedCardPartEntity> allParts;
}
