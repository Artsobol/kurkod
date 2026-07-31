package io.github.artsobol.kurkod.feature.employmentcontract.mapper;

import io.github.artsobol.kurkod.feature.employmentcontract.dto.response.EmploymentContractResponse;
import io.github.artsobol.kurkod.feature.employmentcontract.entity.EmploymentContract;
import io.github.artsobol.kurkod.feature.employmentcontract.dto.request.EmploymentContractUpdateRequest;
import io.github.artsobol.kurkod.feature.employmentcontract.dto.request.EmploymentContractCreateRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface EmploymentContractMapper {

    @Mapping(target = "position", source = "staff.position")
    @Mapping(target = "firstNameWorker", source = "worker.firstName")
    @Mapping(target = "lastNameWorker", source = "worker.lastName")
    EmploymentContractResponse toResponse(EmploymentContract employmentContract);

    @Mapping(target = "staff", ignore = true)
    EmploymentContract toEntity(EmploymentContractCreateRequest employmentContractCreateRequest);
    @Mapping(target = "staff", ignore = true)
    void updatePartially(@MappingTarget EmploymentContract employmentContract, EmploymentContractUpdateRequest employmentContractUpdateRequest);
}
