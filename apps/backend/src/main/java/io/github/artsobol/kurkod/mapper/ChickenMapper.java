package io.github.artsobol.kurkod.mapper;

import io.github.artsobol.kurkod.model.dto.chicken.ChickenDTO;
import io.github.artsobol.kurkod.model.entity.Chicken;
import io.github.artsobol.kurkod.model.request.chicken.ChickenRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring",
nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ChickenMapper {

    @Mapping(source = "breed", target = "breed", ignore = true)
    ChickenDTO toDto(Chicken chicken);

    Chicken toEntity(ChickenRequest chickenRequest);
}
