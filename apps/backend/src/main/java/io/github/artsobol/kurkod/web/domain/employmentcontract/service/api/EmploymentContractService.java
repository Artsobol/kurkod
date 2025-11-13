package io.github.artsobol.kurkod.web.domain.employmentcontract.service.api;

import io.github.artsobol.kurkod.web.domain.employmentcontract.model.dto.EmploymentContractDTO;
import io.github.artsobol.kurkod.web.domain.employmentcontract.model.request.EmploymentContractPatchRequest;
import io.github.artsobol.kurkod.web.domain.employmentcontract.model.request.EmploymentContractPostRequest;
import io.github.artsobol.kurkod.web.domain.employmentcontract.model.request.EmploymentContractPutRequest;

public interface EmploymentContractService {

    EmploymentContractDTO get(Integer workerId);

    EmploymentContractDTO create(Integer workerId, EmploymentContractPostRequest request);

    EmploymentContractDTO replace(Integer workerId, EmploymentContractPutRequest request, Long expectedVersion);

    EmploymentContractDTO update(Integer workerId, EmploymentContractPatchRequest request, Long expectedVersion);

    void delete(Integer workerId, Long expectedVersion);
}
