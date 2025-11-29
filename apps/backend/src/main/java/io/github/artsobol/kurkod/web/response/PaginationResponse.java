package io.github.artsobol.kurkod.web.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaginationResponse<T> {
    private List<T> content;
    private Pagination pagination;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Pagination {
        private long totalElements;
        private int pageSize;
        private int page;
        private int totalPages;
    }

    public static <T> PaginationResponse<T> fromPage(Page<T> page) {
        Pagination pagination = new Pagination(
                page.getTotalElements(),
                page.getSize(),
                page.getNumber(),
                page.getTotalPages()
        );

        return new PaginationResponse<>(page.getContent(), pagination);
    }
}
