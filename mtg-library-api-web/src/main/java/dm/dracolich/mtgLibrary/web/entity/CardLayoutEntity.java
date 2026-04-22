package dm.dracolich.mtgLibrary.web.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDate;

@Document(collection = "card_layouts")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CardLayoutEntity {
    @Id
    private String id;
    @Indexed(unique = true)
    private String code;
    private String name;
    @Field("first_seen")
    private LocalDate firstSeen;
}
