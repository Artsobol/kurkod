package io.github.artsobol.kurkod.feature.rows.dto.response;

import java.time.Instant;

public record RowsResponse(
    Integer id,
    Integer rowNumber,
    Long workshopId,
    Instant createdAt,
    Instant updatedAt,
    Long version) {}
