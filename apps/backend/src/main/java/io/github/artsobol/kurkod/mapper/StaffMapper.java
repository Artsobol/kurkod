package io.github.artsobol.kurkod.mapper;

import io.github.artsobol.kurkod.model.dto.staff.StaffDTO;
import io.github.artsobol.kurkod.model.entity.Staff;
import io.github.artsobol.kurkod.model.request.staff.StaffPatchRequest;
import io.github.artsobol.kurkod.model.request.staff.StaffPostRequest;
import io.github.artsobol.kurkod.model.request.staff.StaffPutRequest;
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
