package io.github.artsobol.kurkod.feature.chickenmovement.service;

import io.github.artsobol.kurkod.feature.chickenmovement.dto.response.ChickenMovementDTO;
import io.github.artsobol.kurkod.feature.chickenmovement.entity.ChickenMovement;
import io.github.artsobol.kurkod.feature.chickenmovement.dto.request.ChickenMovementPostRequest;

import java.time.LocalDateTime;
import java.util.List;

public interface ChickenMovementService {

    ChickenMovementDTO get(Long movementId);

    ChickenMovementDTO getCurrentCage(Long chickenId);

    List<ChickenMovementDTO> getAllByChickenId(Long chickenId);

    ChickenMovementDTO create(Long chickenId, ChickenMovementPostRequest request);
}
