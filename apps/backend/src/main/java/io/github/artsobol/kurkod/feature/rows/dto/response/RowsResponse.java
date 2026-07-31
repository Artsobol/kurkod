package io.github.artsobol.kurkod.feature.rows.dto.response;

import java.time.OffsetDateTime;

public record RowsResponse(
        Integer id, Integer rowNumber, Long workshopId, OffsetDateTime createdAt, OffsetDateTime updatedAt, Long version
) {
}
