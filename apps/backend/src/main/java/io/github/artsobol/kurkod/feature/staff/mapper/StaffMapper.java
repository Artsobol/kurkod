package io.github.artsobol.kurkod.feature.staff.mapper;

import io.github.artsobol.kurkod.feature.staff.dto.response.StaffDTO;
import io.github.artsobol.kurkod.feature.staff.entity.Staff;
import io.github.artsobol.kurkod.feature.staff.dto.request.StaffUpdateRequest;
import io.github.artsobol.kurkod.feature.staff.dto.request.StaffCreateRequest;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring",
nullValuePropertyMappingStrategy = org.mapstruct.NullValuePropertyMappingStrategy.IGNORE)
public interface StaffMapper {

    StaffDTO toDto(Staff staff);

    Staff toEntity(StaffCreateRequest staffCreateRequest);
    void updatePartially(@MappingTarget Staff staff, StaffUpdateRequest staffUpdateRequest);
}
