package io.github.artsobol.kurkod.mapper;

import io.github.artsobol.kurkod.model.dto.chicken.ChickenDTO;
import io.github.artsobol.kurkod.model.entity.Chicken;
import io.github.artsobol.kurkod.model.request.chicken.ChickenPatchRequest;
import io.github.artsobol.kurkod.model.request.chicken.ChickenPostRequest;
import io.github.artsobol.kurkod.model.request.chicken.ChickenPutRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring",
nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ChickenMapper {

    @Mapping(source = "breed.id", target = "breedId")
    ChickenDTO toDto(Chicken chicken);

    Chicken toEntity(ChickenPostRequest chickenPostRequest);

    @Mapping(target = "breed", ignore = true)
    void updateFully(@MappingTarget Chicken chicken, ChickenPutRequest chickenPutRequest);

    @Mapping(target = "breed", ignore = true)
    void updatePartially(@MappingTarget Chicken chicken, ChickenPatchRequest chickenPatchRequest);
}
