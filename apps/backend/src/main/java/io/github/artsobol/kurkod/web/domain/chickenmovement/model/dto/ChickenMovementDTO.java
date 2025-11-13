package io.github.artsobol.kurkod.web.domain.chickenmovement.model.dto;

import java.time.OffsetDateTime;

public record ChickenMovementDTO(
        Integer id, Integer chickenId, Integer fromCageId, Integer toCageId, OffsetDateTime movedAt
) {
};
