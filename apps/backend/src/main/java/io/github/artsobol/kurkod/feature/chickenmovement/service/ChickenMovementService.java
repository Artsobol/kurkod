package io.github.artsobol.kurkod.feature.chickenmovement.service;

import io.github.artsobol.kurkod.feature.chickenmovement.dto.response.ChickenMovementResponse;
import io.github.artsobol.kurkod.feature.chickenmovement.entity.ChickenMovement;
import io.github.artsobol.kurkod.feature.chickenmovement.dto.request.ChickenMovementCreateRequest;

import java.time.LocalDateTime;
import java.util.List;

public interface ChickenMovementService {

    ChickenMovementResponse get(Long movementId);

    ChickenMovementResponse getCurrentCage(Long chickenId);

    List<ChickenMovementResponse> getAllByChickenId(Long chickenId);

    ChickenMovementResponse create(Long chickenId, ChickenMovementCreateRequest request);
}
