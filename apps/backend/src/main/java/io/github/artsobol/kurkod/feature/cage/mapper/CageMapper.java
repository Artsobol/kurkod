package io.github.artsobol.kurkod.feature.cage.mapper;

import io.github.artsobol.kurkod.feature.cage.dto.response.CageDTO;
import io.github.artsobol.kurkod.feature.cage.entity.Cage;
import io.github.artsobol.kurkod.feature.cage.dto.request.CagePatchRequest;
import io.github.artsobol.kurkod.feature.cage.dto.request.CagePostRequest;
import io.github.artsobol.kurkod.feature.cage.dto.request.CagePutRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CageMapper {

    @Mapping(target = "rowId", source = "row.id")
    CageDTO toDto(Cage cage);

    @Mapping(target = "row", ignore = true)
    Cage toEntity(CagePostRequest cagePostRequest);

    @Mapping(target = "row", ignore = true)
    void replace(@MappingTarget Cage cage, CagePutRequest cagePutRequest);

    @Mapping(target = "row", ignore = true)
    void update(@MappingTarget Cage cage, CagePatchRequest cagePatchRequest);
}
