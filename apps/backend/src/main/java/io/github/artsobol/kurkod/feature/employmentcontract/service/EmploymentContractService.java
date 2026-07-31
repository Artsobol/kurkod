package io.github.artsobol.kurkod.feature.employmentcontract.service;

import io.github.artsobol.kurkod.feature.employmentcontract.dto.request.EmploymentContractCreateRequest;
import io.github.artsobol.kurkod.feature.employmentcontract.dto.request.EmploymentContractUpdateRequest;
import io.github.artsobol.kurkod.feature.employmentcontract.dto.response.EmploymentContractResponse;

public interface EmploymentContractService {

    EmploymentContractResponse get(Long workerId);

    EmploymentContractResponse create(Long workerId, EmploymentContractCreateRequest request);
    EmploymentContractResponse update(Long workerId, EmploymentContractUpdateRequest request, Long expectedVersion);

    void delete(Long workerId, Long expectedVersion);
}
