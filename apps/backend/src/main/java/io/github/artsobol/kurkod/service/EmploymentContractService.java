package io.github.artsobol.kurkod.service;

import io.github.artsobol.kurkod.model.dto.employmentContract.EmploymentContractDTO;
import io.github.artsobol.kurkod.model.request.employmentContract.EmploymentContractPatchRequest;
import io.github.artsobol.kurkod.model.request.employmentContract.EmploymentContractPostRequest;
import io.github.artsobol.kurkod.model.request.employmentContract.EmploymentContractPutRequest;
import io.github.artsobol.kurkod.model.response.IamResponse;
import jakarta.validation.constraints.NotNull;

public interface EmploymentContractService {

    IamResponse<EmploymentContractDTO> getByWorkerId(@NotNull Integer workerId);

    IamResponse<EmploymentContractDTO> createEmploymentContract(@NotNull Integer workerId, EmploymentContractPostRequest employmentContractPostRequest);

    IamResponse<EmploymentContractDTO> updateFullyEmploymentContract(@NotNull Integer workerId, EmploymentContractPutRequest employmentContractPutRequest);

    IamResponse<EmploymentContractDTO> updatePartiallyEmploymentContract(@NotNull Integer workerId, EmploymentContractPatchRequest employmentContractPatchRequest);

    void deleteEmploymentContract(@NotNull Integer workerId);
}
