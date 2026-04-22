package dm.dracolich.mtgLibrary.web.entity;

import dm.dracolich.mtgLibrary.dto.enums.CollectionSetType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDate;

@Document(collection = "collection_sets")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CollectionSetEntity {
    @Id
    private String id;
    @Indexed(unique = true)
    @Field("scryfall_id")
    private String scryfallId;
    private String name;
    @Indexed(unique = true)
    private String code;
    @Field("parent_set_code")
    private String parentSetCode;
    private String block;
    @Field("collection_set_type")
    private CollectionSetType collectionSetType;
    @Field("release_date")
    private LocalDate releaseDate;
    @Field("icon_svg_uri")
    private String iconSvgUri;
    @Field("card_count")
    private Integer cardCount;
    private Boolean digital;
    @Field("foil_only")
    private Boolean foilOnly;
    @Field("non_foil_only")
    private Boolean nonFoilOnly;
}
