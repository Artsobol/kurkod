package io.github.artsobol.kurkod.feature.breed.mapper;

import io.github.artsobol.kurkod.feature.breed.dto.response.BreedResponse;
import io.github.artsobol.kurkod.feature.breed.entity.Breed;
import io.github.artsobol.kurkod.feature.breed.dto.request.BreedUpdateRequest;
import io.github.artsobol.kurkod.feature.breed.dto.request.BreedCreateRequest;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface BreedMapper {

    BreedResponse toResponse(Breed breed);

    Breed toEntity(BreedCreateRequest breedCreateRequest);
    void updatePartially(@MappingTarget Breed breed, BreedUpdateRequest breedUpdateRequest);
}
