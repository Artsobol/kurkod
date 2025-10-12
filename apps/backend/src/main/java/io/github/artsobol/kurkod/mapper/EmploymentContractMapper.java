package io.github.artsobol.kurkod.mapper;

import io.github.artsobol.kurkod.model.dto.employmentContract.EmploymentContractDTO;
import io.github.artsobol.kurkod.model.entity.EmploymentContract;
import io.github.artsobol.kurkod.model.request.employmentContract.EmploymentContractPatchRequest;
import io.github.artsobol.kurkod.model.request.employmentContract.EmploymentContractPostRequest;
import io.github.artsobol.kurkod.model.request.employmentContract.EmploymentContractPutRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface EmploymentContractMapper {

    @Mapping(target = "position", source = "staff.position")
    @Mapping(target = "firstNameWorker", source = "worker.firstName")
    @Mapping(target = "lastNameWorker", source = "worker.lastName")
    EmploymentContractDTO toDto(EmploymentContract employmentContract);

    @Mapping(target = "staff", ignore = true)
    EmploymentContract toEntity(EmploymentContractPostRequest employmentContractPostRequest);

    @Mapping(target = "staff", ignore = true)
    void updateFully(@MappingTarget EmploymentContract employmentContract, EmploymentContractPutRequest employmentContractPutRequest);

    @Mapping(target = "staff", ignore = true)
    void updatePartially(@MappingTarget EmploymentContract employmentContract, EmploymentContractPatchRequest employmentContractPatchRequest);
}
