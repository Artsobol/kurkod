package io.github.artsobol.kurkod.feature.passport.dto.response;

import java.time.Instant;

public record PassportResponse(
    String series,
    String number,
    Instant createdAt,
    Instant updatedAt,
    Long version) {}
