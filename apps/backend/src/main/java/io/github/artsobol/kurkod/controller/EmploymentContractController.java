package io.github.artsobol.kurkod.controller;

import io.github.artsobol.kurkod.model.dto.employmentContract.EmploymentContractDTO;
import io.github.artsobol.kurkod.model.request.employmentContract.EmploymentContractPatchRequest;
import io.github.artsobol.kurkod.model.request.employmentContract.EmploymentContractPostRequest;
import io.github.artsobol.kurkod.model.request.employmentContract.EmploymentContractPutRequest;
import io.github.artsobol.kurkod.model.response.IamResponse;
import io.github.artsobol.kurkod.service.EmploymentContractService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/workers/{workerId}/contract", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Employment Contract", description = "Employment Contract operations")
public class EmploymentContractController {

    private final EmploymentContractService employmentContractService;

    @Operation(
            summary = "Get employment contract by worker ID",
            description = "Returns the employment contract information for the specified worker."
    )
    @GetMapping
    public ResponseEntity<IamResponse<EmploymentContractDTO>> get(@Parameter(
            description = "Worker identifier",
            example = "12"
    ) @PathVariable(name = "workerId") Integer workerId) {
        EmploymentContractDTO response = employmentContractService.get(workerId);
        return ResponseEntity.ok(IamResponse.createSuccessful(response));
    }

    @Operation(
            summary = "Create an employment contract for a worker",
            description = "Creates a new employment contract for the specified worker. Each worker can have only one active contract."
    )
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<IamResponse<EmploymentContractDTO>> create(@PathVariable(name = "workerId") Integer workerId,
                                                                     @RequestBody @Valid EmploymentContractPostRequest employmentContractPostRequest) {
        EmploymentContractDTO response = employmentContractService.create(workerId,
                employmentContractPostRequest);
        return ResponseEntity.ok(IamResponse.createSuccessful(response));
    }


    @Operation(
            summary = "Replace an employment contract",
            description = "Fully replaces the employment contract data for the specified worker."
    )
    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<IamResponse<EmploymentContractDTO>> replace(@Parameter(
                                                                              description = "Worker identifier",
                                                                              example = "12"
                                                                      ) @PathVariable(name = "workerId") Integer workerId,
                                                                      @RequestBody @Valid EmploymentContractPutRequest employmentContractPutRequest) {
        EmploymentContractDTO response = employmentContractService.replace(workerId,
                employmentContractPutRequest);
        return ResponseEntity.ok(IamResponse.createSuccessful(response));
    }

    @Operation(
            summary = "Partially update an employment contract",
            description = "Applies a partial update to the employment contract for the specified worker."
    )
    @PatchMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<IamResponse<EmploymentContractDTO>> update(@Parameter(
                                                                             description = "Worker identifier",
                                                                             example = "12"
                                                                     ) @PathVariable(name = "workerId") Integer workerId,
                                                                     @RequestBody @Valid EmploymentContractPatchRequest employmentContractPatchRequest) {
        EmploymentContractDTO response = employmentContractService.update(
                workerId,
                employmentContractPatchRequest);
        return ResponseEntity.ok(IamResponse.createSuccessful(response));
    }

    @Operation(
            summary = "Delete an employment contract",
            description = "Deletes the employment contract associated with the specified worker."
    )
    @DeleteMapping
    public ResponseEntity<Void> delete(@Parameter(
            description = "Worker identifier",
            example = "12"
    ) @PathVariable(name = "workerId") Integer workerId) {
        employmentContractService.delete(workerId);
        return ResponseEntity.noContent().build();
    }
}
