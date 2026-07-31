package io.github.artsobol.kurkod.feature.chickenmovement.mapper;

import io.github.artsobol.kurkod.feature.chickenmovement.dto.response.ChickenMovementDTO;
import io.github.artsobol.kurkod.feature.chickenmovement.entity.ChickenMovement;
import io.github.artsobol.kurkod.feature.chickenmovement.dto.request.ChickenMovementCreateRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ChickenMovementMapper {

    @Mapping(target = "fromCageId", source = "fromCage.id")
    @Mapping(target = "toCageId", source = "toCage.id")
    @Mapping(target = "chickenId", source = "chicken.id")
    ChickenMovementDTO toDto(ChickenMovement chickenMovement);

    @Mapping(target = "chicken", ignore = true)
    @Mapping(target = "fromCage", ignore = true)
    @Mapping(target = "toCage", ignore = true)
    ChickenMovement toEntity(ChickenMovementCreateRequest chickenMovementCreateRequest);
}
