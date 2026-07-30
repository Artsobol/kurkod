package io.github.artsobol.kurkod.feature.breed.mapper;

import io.github.artsobol.kurkod.feature.breed.dto.response.BreedDTO;
import io.github.artsobol.kurkod.feature.breed.entity.Breed;
import io.github.artsobol.kurkod.feature.breed.dto.request.BreedPatchRequest;
import io.github.artsobol.kurkod.feature.breed.dto.request.BreedPostRequest;
import io.github.artsobol.kurkod.feature.breed.dto.request.BreedPutRequest;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface BreedMapper {

    BreedDTO toDto(Breed breed);

    Breed toEntity(BreedPostRequest breedPostRequest);

    void updateFully(@MappingTarget Breed breed, BreedPutRequest breedPutRequest);

    void updatePartially(@MappingTarget Breed breed, BreedPatchRequest breedPatchRequest);
}
