package io.github.artsobol.kurkod.feature.breed.mapper;

import io.github.artsobol.kurkod.config.mapstruct.MapStructConfig;
import io.github.artsobol.kurkod.feature.breed.dto.response.BreedResponse;
import io.github.artsobol.kurkod.feature.breed.entity.Breed;
import org.mapstruct.Mapper;

@Mapper(config = MapStructConfig.class)
public interface BreedMapper {

  BreedResponse toResponse(Breed breed);
}
