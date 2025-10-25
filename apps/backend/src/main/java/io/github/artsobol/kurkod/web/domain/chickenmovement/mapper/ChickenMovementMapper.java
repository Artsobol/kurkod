package io.github.artsobol.kurkod.web.domain.chickenmovement.mapper;

import io.github.artsobol.kurkod.web.domain.chickenmovement.model.dto.ChickenMovementDTO;
import io.github.artsobol.kurkod.web.domain.chickenmovement.model.entity.ChickenMovement;
import io.github.artsobol.kurkod.web.domain.chickenmovement.model.request.ChickenMovementPostRequest;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ChickenMovementMapper {

    ChickenMovementDTO toDto(ChickenMovement chickenMovement);

    ChickenMovement toEntity(ChickenMovementPostRequest chickenMovementPostRequest);
}
