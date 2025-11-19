package io.github.artsobol.kurkod.web.domain.rows.model.dto;

import java.time.OffsetDateTime;

public record RowsDTO(
        Integer id, Integer rowNumber, Integer workshopId, OffsetDateTime createdAt, OffsetDateTime updatedAt, Long version
) {
}
