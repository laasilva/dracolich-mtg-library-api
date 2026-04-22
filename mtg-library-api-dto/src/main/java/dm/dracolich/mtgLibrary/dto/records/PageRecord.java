package dm.dracolich.mtgLibrary.dto.records;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record PageRecord<T>(
        List<T> content,
        int page,
        int size,
        @JsonProperty("total_elements") long totalElements,
        @JsonProperty("total_pages") int totalPages
) {
    public static <T> PageRecord<T> of(List<T> content, int page, int size, long totalElements) {
        int totalPages = size == 0 ? 0 : (int) Math.ceil((double) totalElements / size);
        return new PageRecord<>(content, page, size, totalElements, totalPages);
    }
}
