package io.github.artsobol.kurkod.controller;

import io.github.artsobol.kurkod.model.constants.ApiLogMessage;
import io.github.artsobol.kurkod.model.dto.worker.WorkerDTO;
import io.github.artsobol.kurkod.model.request.worker.WorkerPatchRequest;
import io.github.artsobol.kurkod.model.request.worker.WorkerPostRequest;
import io.github.artsobol.kurkod.model.request.worker.WorkerPutRequest;
import io.github.artsobol.kurkod.model.response.IamResponse;
import io.github.artsobol.kurkod.service.WorkerService;
import io.github.artsobol.kurkod.utils.ApiUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(value = "/api/v1/workers", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@Tag(name = "Workers", description = "Worker operations")
public class WorkerController {

    private final WorkerService workerService;

    @Operation(summary = "List all workers", description = "Returns all workers.")
    @GetMapping
    public ResponseEntity<IamResponse<List<WorkerDTO>>> getAll() {
        log.trace(ApiLogMessage.NAME_OF_CURRENT_METHOD.getValue(), ApiUtils.getMethodName());
        List<WorkerDTO> response = workerService.getAll();
        return ResponseEntity.ok(IamResponse.createSuccessful(response));
    }

    @Operation(summary = "Get worker by ID", description = "Returns a single worker by its unique identifier.")
    @GetMapping("/{id}")
    public ResponseEntity<IamResponse<WorkerDTO>> get(@Parameter(
            description = "Worker identifier",
            example = "42"
    ) @PathVariable(name = "id") Integer id) {
        log.trace(ApiLogMessage.NAME_OF_CURRENT_METHOD.getValue(), ApiUtils.getMethodName());
        WorkerDTO response = workerService.get(id);
        return ResponseEntity.ok(IamResponse.createSuccessful(response));
    }

    @Operation(summary = "Create a worker", description = "Creates a new worker entity.")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<IamResponse<WorkerDTO>> create(@RequestBody @Valid WorkerPostRequest workerPostRequest) {
        log.trace(ApiLogMessage.NAME_OF_CURRENT_METHOD.getValue(), ApiUtils.getMethodName());
        WorkerDTO response = workerService.create(workerPostRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(IamResponse.createSuccessful(response));
    }

    @Operation(summary = "Replace a worker", description = "Fully replaces a worker by ID.")
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<IamResponse<WorkerDTO>> replace(@Parameter(
                                                                            description = "Worker identifier",
                                                                            example = "42"
                                                                    ) @PathVariable(name = "id") Integer id,
                                                                    @RequestBody @Valid WorkerPutRequest workerPutRequest) {
        log.trace(ApiLogMessage.NAME_OF_CURRENT_METHOD.getValue(), ApiUtils.getMethodName());
        WorkerDTO response = workerService.replace(id, workerPutRequest);
        return ResponseEntity.ok(IamResponse.createSuccessful(response));
    }

    @Operation(summary = "Partially update a worker", description = "Applies a partial update to a worker by ID.")
    @PatchMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<IamResponse<WorkerDTO>> update(@Parameter(
                                                                                description = "Worker identifier",
                                                                                example = "42"
                                                                        ) @PathVariable(name = "id") Integer id,
                                                                        @RequestBody @Valid WorkerPatchRequest workerPatchRequest) {
        log.trace(ApiLogMessage.NAME_OF_CURRENT_METHOD.getValue(), ApiUtils.getMethodName());
        WorkerDTO response = workerService.update(id, workerPatchRequest);
        return ResponseEntity.ok(IamResponse.createSuccessful(response));
    }

    @Operation(summary = "Delete a worker", description = "Deletes a worker by its unique identifier.")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@Parameter(
            description = "Worker identifier",
            example = "42"
    ) @PathVariable(name = "id") Integer id) {
        log.trace(ApiLogMessage.NAME_OF_CURRENT_METHOD.getValue(), ApiUtils.getMethodName());
        workerService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
