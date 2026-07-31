package io.github.artsobol.kurkod.feature.diet.mapper;

import io.github.artsobol.kurkod.feature.diet.dto.response.DietResponse;
import io.github.artsobol.kurkod.feature.diet.entity.Diet;
import io.github.artsobol.kurkod.feature.diet.dto.request.DietUpdateRequest;
import io.github.artsobol.kurkod.feature.diet.dto.request.DietCreateRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface DietMapper {

    DietResponse toResponse(Diet diet);

    @Mapping(target = "breeds", ignore = true)
    Diet toEntity(DietCreateRequest dietCreateRequest);

    @Mapping(target = "breeds", ignore = true)
    void update(@MappingTarget Diet diet, DietUpdateRequest dietUpdateRequest);
}
