package io.github.artsobol.kurkod.mapper;

import io.github.artsobol.kurkod.model.dto.Breed.BreedDTO;
import io.github.artsobol.kurkod.model.entity.Breed;
import io.github.artsobol.kurkod.model.request.breed.BreedRequest;
import io.github.artsobol.kurkod.repository.BreedRepository;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface BreedMapper {

    BreedDTO toDto(Breed breed);

    Breed toEntity(BreedRequest breedRequest);

}
