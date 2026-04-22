package dm.dracolich.mtgLibrary.web.entity;

import dm.dracolich.mtgLibrary.dto.enums.RelatedCardComponent;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RelatedCardPartEntity {
    @Field("related_card_id")
    private String relatedCardId;
    private RelatedCardComponent component;
    private String name;
    @Field("type_line")
    private String typeLine;
}
