package io.github.artsobol.kurkod.controller;

import io.github.artsobol.kurkod.model.dto.employmentContract.EmploymentContractDTO;
import io.github.artsobol.kurkod.model.request.employmentContract.EmploymentContractPatchRequest;
import io.github.artsobol.kurkod.model.request.employmentContract.EmploymentContractPostRequest;
import io.github.artsobol.kurkod.model.request.employmentContract.EmploymentContractPutRequest;
import io.github.artsobol.kurkod.model.response.IamResponse;
import io.github.artsobol.kurkod.service.EmploymentContractService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/workers/{workerId}/contract")
public class EmploymentContractController {

    private final EmploymentContractService employmentContractService;

    @GetMapping
    public ResponseEntity<IamResponse<EmploymentContractDTO>> getEmploymentContractById(@PathVariable(name = "workerId") Integer workerId){
        IamResponse<EmploymentContractDTO> response = employmentContractService.getByWorkerId(workerId);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<IamResponse<EmploymentContractDTO>> createEmploymentContract(@PathVariable(name = "workerId") Integer workerId, @RequestBody @Valid EmploymentContractPostRequest employmentContractPostRequest){
        IamResponse<EmploymentContractDTO> response = employmentContractService.createEmploymentContract(workerId, employmentContractPostRequest);
        return ResponseEntity.ok(response);
    }

    @PutMapping
    public ResponseEntity<IamResponse<EmploymentContractDTO>> updateFullyEmploymentContract(@PathVariable(name = "workerId") Integer workerId, @RequestBody @Valid EmploymentContractPutRequest employmentContractPutRequest){
        IamResponse<EmploymentContractDTO> response = employmentContractService.updateFullyEmploymentContract(workerId, employmentContractPutRequest);
        return ResponseEntity.ok(response);
    }

    @PatchMapping
    public ResponseEntity<IamResponse<EmploymentContractDTO>> updatePartiallyEmploymentContract(@PathVariable(name = "workerId") Integer workerId, @RequestBody @Valid EmploymentContractPatchRequest employmentContractPatchRequest){
        IamResponse<EmploymentContractDTO> response = employmentContractService.updatePartiallyEmploymentContract(workerId, employmentContractPatchRequest);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteEmploymentContract(@PathVariable(name = "workerId") Integer workerId){
        employmentContractService.deleteEmploymentContract(workerId);
        return ResponseEntity.noContent().build();
    }
}
