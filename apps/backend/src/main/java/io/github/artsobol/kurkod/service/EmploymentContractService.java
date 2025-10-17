package io.github.artsobol.kurkod.service;

import io.github.artsobol.kurkod.model.dto.employmentContract.EmploymentContractDTO;
import io.github.artsobol.kurkod.model.request.employmentContract.EmploymentContractPatchRequest;
import io.github.artsobol.kurkod.model.request.employmentContract.EmploymentContractPostRequest;
import io.github.artsobol.kurkod.model.request.employmentContract.EmploymentContractPutRequest;

public interface EmploymentContractService {

    EmploymentContractDTO get(Integer workerId);

    EmploymentContractDTO create(Integer workerId,
                                              EmploymentContractPostRequest employmentContractPostRequest);

    EmploymentContractDTO replace(Integer workerId,
                                               EmploymentContractPutRequest employmentContractPutRequest);

    EmploymentContractDTO update(Integer workerId,
                                              EmploymentContractPatchRequest employmentContractPatchRequest);

    void delete(Integer workerId);
}
