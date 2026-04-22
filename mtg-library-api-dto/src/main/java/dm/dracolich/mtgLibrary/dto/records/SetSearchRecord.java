package dm.dracolich.mtgLibrary.dto.records;

import com.fasterxml.jackson.annotation.JsonProperty;
import dm.dracolich.mtgLibrary.dto.enums.CollectionSetType;

import java.time.LocalDate;

public record SetSearchRecord(
        String code,
        String block,
        @JsonProperty("collection_set_type") CollectionSetType collectionSetType,
        @JsonProperty("released_after") LocalDate releasedAfter,
        @JsonProperty("released_before") LocalDate releasedBefore,
        Boolean digital
) {}
