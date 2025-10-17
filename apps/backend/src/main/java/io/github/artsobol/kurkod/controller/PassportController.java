package io.github.artsobol.kurkod.controller;

import io.github.artsobol.kurkod.model.constants.ApiLogMessage;
import io.github.artsobol.kurkod.model.dto.passport.PassportDTO;
import io.github.artsobol.kurkod.model.request.passport.PassportPatchRequest;
import io.github.artsobol.kurkod.model.request.passport.PassportPostRequest;
import io.github.artsobol.kurkod.model.request.passport.PassportPutRequest;
import io.github.artsobol.kurkod.model.response.IamResponse;
import io.github.artsobol.kurkod.service.PassportService;
import io.github.artsobol.kurkod.utils.ApiUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/workers/{workerId}/passport", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Passport", description = "Passport operations")
public class PassportController {

    private final PassportService passportService;

    @Operation(
            summary = "Get passport by worker ID",
            description = "Returns the passport information for a specific worker."
    )
    @GetMapping
    public ResponseEntity<IamResponse<PassportDTO>> get(@Parameter(
            description = "Worker identifier",
            example = "5"
    ) @PathVariable(name = "workerId") Integer workerId) {
        log.trace(ApiLogMessage.NAME_OF_CURRENT_METHOD.getValue(), ApiUtils.getMethodName());
        PassportDTO response = passportService.get(workerId);
        return ResponseEntity.ok(IamResponse.createSuccessful(response));
    }

    @Operation(
            summary = "Create a passport for a worker",
            description = "Creates a new passport for the specified worker. Each worker can have only one passport."
    )
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<IamResponse<PassportDTO>> create(@Parameter(
            description = "Worker identifier",
            example = "5"
    ) @PathVariable(name = "workerId") Integer workerId, @RequestBody @Valid PassportPostRequest passportPostRequest) {
        log.trace(ApiLogMessage.NAME_OF_CURRENT_METHOD.getValue(), ApiUtils.getMethodName());
        PassportDTO response = passportService.create(workerId, passportPostRequest);
        return ResponseEntity.ok(IamResponse.createSuccessful(response));
    }

    @Operation(
            summary = "Replace a worker’s passport",
            description = "Fully replaces the passport data for the specified worker."
    )
    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<IamResponse<PassportDTO>> replace(@Parameter(
            description = "Worker identifier",
            example = "5"
    ) @PathVariable(name = "workerId") Integer workerId, @RequestBody @Valid PassportPutRequest passportPutRequest) {
        log.trace(ApiLogMessage.NAME_OF_CURRENT_METHOD.getValue(), ApiUtils.getMethodName());
        PassportDTO response = passportService.replace(workerId, passportPutRequest);
        return ResponseEntity.ok(IamResponse.createSuccessful(response));
    }

    @Operation(
            summary = "Partially update a worker’s passport",
            description = "Applies a partial update to the passport data for the specified worker."
    )
    @PatchMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<IamResponse<PassportDTO>> update(@Parameter(
            description = "Worker identifier",
            example = "5"
    ) @PathVariable(name = "workerId") Integer workerId, @RequestBody PassportPatchRequest passportPatchRequest) {
        log.trace(ApiLogMessage.NAME_OF_CURRENT_METHOD.getValue(), ApiUtils.getMethodName());
        PassportDTO response = passportService.update(workerId, passportPatchRequest);
        return ResponseEntity.ok(IamResponse.createSuccessful(response));
    }

    @Operation(
            summary = "Delete a worker’s passport",
            description = "Deletes the passport associated with the specified worker."
    )
    @DeleteMapping
    public ResponseEntity<Void> delete(@Parameter(
            description = "Worker identifier",
            example = "5"
    ) @PathVariable(name = "workerId") Integer workerId) {
        log.trace(ApiLogMessage.NAME_OF_CURRENT_METHOD.getValue(), ApiUtils.getMethodName());
        passportService.delete(workerId);
        return ResponseEntity.noContent().build();
    }
}
