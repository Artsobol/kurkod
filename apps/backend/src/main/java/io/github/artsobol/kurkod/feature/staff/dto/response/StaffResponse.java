package io.github.artsobol.kurkod.feature.staff.dto.response;

import java.time.Instant;

public record StaffResponse(
    Long id, String position, Instant createdAt, Instant updatedAt, Long version) {}
