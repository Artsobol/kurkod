package io.github.artsobol.kurkod.feature.passport.dto.response;


import java.time.OffsetDateTime;

public record PassportResponse(
        String series, String number, OffsetDateTime createdAt, OffsetDateTime updatedAt, Long version
) {
};