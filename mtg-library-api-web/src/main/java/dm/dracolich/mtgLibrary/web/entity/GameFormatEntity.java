package dm.dracolich.mtgLibrary.web.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "game_formats")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GameFormatEntity {
    @Id
    private String id;
    @Indexed(unique = true)
    private String code;
    private String name;
    private String description;
    @Field("min_deck_size")
    private Integer minDeckSize;
    @Field("max_deck_size")
    private Integer maxDeckSize;
    @Field("sideboard_size")
    private Integer sideboardSize;
    private Boolean singleton;
    @Field("uses_color_identity")
    private Boolean usesColorIdentity;
    @Field("commander_count")
    private Integer commanderCount;
    @Field("starting_life")
    private Integer startingLife;
    @Field("max_copies")
    private Integer maxCopies;
    private Boolean digital;
    private Boolean active;
}
