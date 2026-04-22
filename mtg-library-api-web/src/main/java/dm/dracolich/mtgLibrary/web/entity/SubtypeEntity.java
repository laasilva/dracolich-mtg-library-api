package dm.dracolich.mtgLibrary.web.entity;

import dm.dracolich.mtgLibrary.dto.enums.SubtypeCategory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDate;

@Document(collection = "subtypes")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SubtypeEntity {
    @Id
    private String id;
    @Indexed(unique = true)
    private String code;
    private String name;
    @Indexed
    private SubtypeCategory category;
    @Field("first_seen")
    private LocalDate firstSeen;
}
