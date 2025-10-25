package io.github.artsobol.kurkod.web.domain.chickenmovement.service.api;

import io.github.artsobol.kurkod.web.domain.chickenmovement.model.dto.ChickenMovementDTO;
import io.github.artsobol.kurkod.web.domain.chickenmovement.model.entity.ChickenMovement;
import io.github.artsobol.kurkod.web.domain.chickenmovement.model.request.ChickenMovementPostRequest;

import java.time.LocalDateTime;
import java.util.List;

public interface ChickenMovementService {

    ChickenMovementDTO get(Integer movementId);

    ChickenMovementDTO getCurrentCage(Integer chickenId);

    List<ChickenMovementDTO> getAllByChickenId(Integer chickenId);

    ChickenMovementDTO create(Integer chickenId, ChickenMovementPostRequest request);
}
