package io.github.artsobol.kurkod.feature.chickenmovement.service;

import io.github.artsobol.kurkod.feature.chickenmovement.dto.request.ChickenMovementCreateRequest;
import io.github.artsobol.kurkod.feature.chickenmovement.dto.response.ChickenMovementResponse;
import java.util.List;

public interface ChickenMovementService {

    ChickenMovementResponse get(Long movementId);

    ChickenMovementResponse getCurrentCage(Long chickenId);

    List<ChickenMovementResponse> getAllByChickenId(Long chickenId);

    ChickenMovementResponse create(Long chickenId, ChickenMovementCreateRequest request);
}
