package io.github.artsobol.kurkod.mapper;

import io.github.artsobol.kurkod.model.dto.breed.BreedDTO;
import io.github.artsobol.kurkod.model.entity.Breed;
import io.github.artsobol.kurkod.model.request.breed.BreedRequest;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface BreedMapper {

    BreedDTO toDto(Breed breed);

    Breed toEntity(BreedRequest breedRequest);

}
