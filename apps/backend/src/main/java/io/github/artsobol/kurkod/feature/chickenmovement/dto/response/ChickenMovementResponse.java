package io.github.artsobol.kurkod.feature.chickenmovement.dto.response;

import java.time.Instant;

public record ChickenMovementResponse(
    Long id, Long chickenId, Long fromCageId, Long toCageId, Instant movedAt) {}
