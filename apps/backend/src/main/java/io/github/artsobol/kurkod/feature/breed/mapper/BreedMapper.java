package io.github.artsobol.kurkod.feature.breed.mapper;

import io.github.artsobol.kurkod.feature.breed.dto.request.BreedCreateRequest;
import io.github.artsobol.kurkod.feature.breed.dto.request.BreedUpdateRequest;
import io.github.artsobol.kurkod.feature.breed.dto.response.BreedResponse;
import io.github.artsobol.kurkod.feature.breed.entity.Breed;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(
    componentModel = "spring",
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface BreedMapper {

  BreedResponse toResponse(Breed breed);

  @Mapping(target = "diets", ignore = true)
  Breed toEntity(BreedCreateRequest breedCreateRequest);

  @Mapping(target = "diets", ignore = true)
  void updatePartially(@MappingTarget Breed breed, BreedUpdateRequest breedUpdateRequest);
}
