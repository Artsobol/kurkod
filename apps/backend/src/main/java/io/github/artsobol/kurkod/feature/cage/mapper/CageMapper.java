package io.github.artsobol.kurkod.feature.cage.mapper;

import io.github.artsobol.kurkod.feature.cage.dto.response.CageResponse;
import io.github.artsobol.kurkod.feature.cage.entity.Cage;
import io.github.artsobol.kurkod.feature.cage.dto.request.CageUpdateRequest;
import io.github.artsobol.kurkod.feature.cage.dto.request.CageCreateRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CageMapper {

  @Mapping(target = "rowId", source = "row.id")
  CageResponse toResponse(Cage cage);

  @Mapping(target = "row", ignore = true)
  Cage toEntity(CageCreateRequest cageCreateRequest);

  @Mapping(target = "row", ignore = true)
  void update(@MappingTarget Cage cage, CageUpdateRequest cageUpdateRequest);
}
