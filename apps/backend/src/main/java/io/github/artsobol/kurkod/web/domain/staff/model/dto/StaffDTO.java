package io.github.artsobol.kurkod.web.domain.staff.model.dto;

import java.time.OffsetDateTime;

public record StaffDTO(
        Integer id, String position, OffsetDateTime createdAt, OffsetDateTime updatedAt, Long version
) {
};
