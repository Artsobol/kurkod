package io.github.artsobol.kurkod.feature.employmentcontract.service;

import io.github.artsobol.kurkod.feature.employmentcontract.dto.response.EmploymentContractDTO;
import io.github.artsobol.kurkod.feature.employmentcontract.dto.request.EmploymentContractUpdateRequest;
import io.github.artsobol.kurkod.feature.employmentcontract.dto.request.EmploymentContractCreateRequest;

public interface EmploymentContractService {

    EmploymentContractDTO get(Long workerId);

    EmploymentContractDTO create(Long workerId, EmploymentContractCreateRequest request);
    EmploymentContractDTO update(Long workerId, EmploymentContractUpdateRequest request, Long expectedVersion);

    void delete(Long workerId, Long expectedVersion);
}
