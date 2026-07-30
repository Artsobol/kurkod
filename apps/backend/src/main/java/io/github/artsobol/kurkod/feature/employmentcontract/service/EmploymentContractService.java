package io.github.artsobol.kurkod.feature.employmentcontract.service;

import io.github.artsobol.kurkod.feature.employmentcontract.dto.response.EmploymentContractDTO;
import io.github.artsobol.kurkod.feature.employmentcontract.dto.request.EmploymentContractPatchRequest;
import io.github.artsobol.kurkod.feature.employmentcontract.dto.request.EmploymentContractPostRequest;
import io.github.artsobol.kurkod.feature.employmentcontract.dto.request.EmploymentContractPutRequest;

public interface EmploymentContractService {

    EmploymentContractDTO get(Long workerId);

    EmploymentContractDTO create(Long workerId, EmploymentContractPostRequest request);

    EmploymentContractDTO replace(Long workerId, EmploymentContractPutRequest request, Long expectedVersion);

    EmploymentContractDTO update(Long workerId, EmploymentContractPatchRequest request, Long expectedVersion);

    void delete(Long workerId, Long expectedVersion);
}
