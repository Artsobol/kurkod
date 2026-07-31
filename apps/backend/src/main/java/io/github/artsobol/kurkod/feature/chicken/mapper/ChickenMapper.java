package io.github.artsobol.kurkod.feature.chicken.mapper;

import io.github.artsobol.kurkod.feature.breed.mapper.BreedMapper;
import io.github.artsobol.kurkod.feature.cage.mapper.CageMapper;
import io.github.artsobol.kurkod.feature.chicken.dto.response.ChickenDTO;
import io.github.artsobol.kurkod.feature.chicken.entity.Chicken;
import io.github.artsobol.kurkod.feature.chicken.dto.request.ChickenUpdateRequest;
import io.github.artsobol.kurkod.feature.chicken.dto.request.ChickenCreateRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        uses = {CageMapper.class, BreedMapper.class})
public interface ChickenMapper {

    ChickenDTO toDto(Chicken chicken);

    Chicken toEntity(ChickenCreateRequest chickenCreateRequest);
    @Mapping(target = "cage", ignore = true)
    @Mapping(target = "breed", ignore = true)
    void updatePartially(@MappingTarget Chicken chicken, ChickenUpdateRequest chickenUpdateRequest);
}
