package io.github.artsobol.kurkod.feature.workshop.mapper;

import io.github.artsobol.kurkod.feature.workshop.dto.response.WorkshopDTO;
import io.github.artsobol.kurkod.feature.workshop.entity.Workshop;
import io.github.artsobol.kurkod.feature.workshop.dto.request.WorkshopPatchRequest;
import io.github.artsobol.kurkod.feature.workshop.dto.request.WorkshopPostRequest;
import io.github.artsobol.kurkod.feature.workshop.dto.request.WorkshopPutRequest;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface WorkshopMapper {

    WorkshopDTO toDto(Workshop workshop);

    Workshop toEntity(WorkshopPostRequest workshopPostRequest);

    void replace(@MappingTarget Workshop workshop, WorkshopPutRequest workshopPutRequest);

    void update(@MappingTarget Workshop workshop, WorkshopPatchRequest workshopPatchRequest);
}
