package io.github.artsobol.kurkod.feature.workshop.mapper;

import io.github.artsobol.kurkod.feature.workshop.dto.request.WorkshopCreateRequest;
import io.github.artsobol.kurkod.feature.workshop.dto.request.WorkshopUpdateRequest;
import io.github.artsobol.kurkod.feature.workshop.dto.response.WorkshopResponse;
import io.github.artsobol.kurkod.feature.workshop.entity.Workshop;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface WorkshopMapper {

  WorkshopResponse toResponse(Workshop workshop);

  @Mapping(target = "rows", ignore = true)
  Workshop toEntity(WorkshopCreateRequest workshopCreateRequest);

  @Mapping(target = "rows", ignore = true)
  void update(@MappingTarget Workshop workshop, WorkshopUpdateRequest workshopUpdateRequest);
}
