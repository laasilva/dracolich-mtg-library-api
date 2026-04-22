package dm.dracolich.mtgLibrary.web.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDate;

@Document(collection = "rulings")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RulingEntity {
    @Id
    private String id;
    @Field("oracle_id")
    private String oracleId;
    private String source;
    private LocalDate published;
    private String comment;
}
