package io.github.artsobol.kurkod.feature.workshop.mapper;

import io.github.artsobol.kurkod.feature.workshop.dto.response.WorkshopDTO;
import io.github.artsobol.kurkod.feature.workshop.entity.Workshop;
import io.github.artsobol.kurkod.feature.workshop.dto.request.WorkshopUpdateRequest;
import io.github.artsobol.kurkod.feature.workshop.dto.request.WorkshopCreateRequest;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface WorkshopMapper {

    WorkshopDTO toDto(Workshop workshop);

    Workshop toEntity(WorkshopCreateRequest workshopCreateRequest);
    void update(@MappingTarget Workshop workshop, WorkshopUpdateRequest workshopUpdateRequest);
}
