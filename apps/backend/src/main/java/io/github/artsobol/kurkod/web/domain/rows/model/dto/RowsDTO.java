package io.github.artsobol.kurkod.web.domain.rows.model.dto;

import java.time.OffsetDateTime;

public record RowsDTO(
        Integer id, Integer rowNumber, Integer workshopNumber, OffsetDateTime createdAt, OffsetDateTime updatedAt, Long version
) {
};
