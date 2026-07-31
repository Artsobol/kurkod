package io.github.artsobol.kurkod.feature.rows.dto.response;

import io.github.artsobol.kurkod.feature.cage.entity.Cage;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.Instant;
import java.util.List;

public record RowsDetailedResponse(
    Long id,
    Integer rowNumber,
    Integer workshopNumber,
    List<Cage> cages,
    Instant createdAt,
    Instant updatedAt) {}
