package io.github.artsobol.kurkod.feature.staff.mapper;

import io.github.artsobol.kurkod.feature.staff.dto.response.StaffDTO;
import io.github.artsobol.kurkod.feature.staff.entity.Staff;
import io.github.artsobol.kurkod.feature.staff.dto.request.StaffPatchRequest;
import io.github.artsobol.kurkod.feature.staff.dto.request.StaffPostRequest;
import io.github.artsobol.kurkod.feature.staff.dto.request.StaffPutRequest;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring",
nullValuePropertyMappingStrategy = org.mapstruct.NullValuePropertyMappingStrategy.IGNORE)
public interface StaffMapper {

    StaffDTO toDto(Staff staff);

    Staff toEntity(StaffPostRequest staffPostRequest);

    void updateFully(@MappingTarget Staff staff, StaffPutRequest staffPutRequest);

    void updatePartially(@MappingTarget Staff staff, StaffPatchRequest staffPatchRequest);
}
