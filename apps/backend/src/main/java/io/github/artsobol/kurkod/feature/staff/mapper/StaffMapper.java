package io.github.artsobol.kurkod.feature.staff.mapper;

import io.github.artsobol.kurkod.feature.staff.dto.request.StaffCreateRequest;
import io.github.artsobol.kurkod.feature.staff.dto.request.StaffUpdateRequest;
import io.github.artsobol.kurkod.feature.staff.dto.response.StaffResponse;
import io.github.artsobol.kurkod.feature.staff.entity.Staff;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring",
nullValuePropertyMappingStrategy = org.mapstruct.NullValuePropertyMappingStrategy.IGNORE)
public interface StaffMapper {

    StaffResponse toResponse(Staff staff);

    Staff toEntity(StaffCreateRequest staffCreateRequest);
    void updatePartially(@MappingTarget Staff staff, StaffUpdateRequest staffUpdateRequest);
}
