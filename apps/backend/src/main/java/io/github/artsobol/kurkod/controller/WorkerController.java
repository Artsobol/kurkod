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
    public ResponseEntity<IamResponse<List<WorkerDTO>>> getAllWorkers() {
        log.trace(ApiLogMessage.NAME_OF_CURRENT_METHOD.getValue(), ApiUtils.getMethodName());
        IamResponse<List<WorkerDTO>> response = workerService.getAllWorkers();
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get worker by ID", description = "Returns a single worker by its unique identifier.")
    @GetMapping("/{id}")
    public ResponseEntity<IamResponse<WorkerDTO>> getWorkerById(@Parameter(description = "Worker identifier", example = "42") @PathVariable(name = "id") Integer id) {
        log.trace(ApiLogMessage.NAME_OF_CURRENT_METHOD.getValue(), ApiUtils.getMethodName());
        IamResponse<WorkerDTO> response = workerService.getWorkerById(id);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Create a worker", description = "Creates a new worker entity.")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<IamResponse<WorkerDTO>> createWorker(@RequestBody @Valid WorkerPostRequest workerPostRequest) {
        log.trace(ApiLogMessage.NAME_OF_CURRENT_METHOD.getValue(), ApiUtils.getMethodName());
        IamResponse<WorkerDTO> response = workerService.createWorker(workerPostRequest);
        return ResponseEntity.status(201).body(response);
    }

    @Operation(summary = "Replace a worker", description = "Fully replaces a worker by ID.")
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<IamResponse<WorkerDTO>> updateFullyWorker(@Parameter(description = "Worker identifier", example = "42") @PathVariable(name = "id") Integer id,
                                                                    @RequestBody @Valid WorkerPutRequest workerPutRequest) {
        log.trace(ApiLogMessage.NAME_OF_CURRENT_METHOD.getValue(), ApiUtils.getMethodName());
        IamResponse<WorkerDTO> response = workerService.updateFullyWorker(id, workerPutRequest);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Partially update a worker", description = "Applies a partial update to a worker by ID.")
    @PatchMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<IamResponse<WorkerDTO>> updatePartiallyWorker(@Parameter(description = "Worker identifier", example = "42") @PathVariable(name = "id") Integer id,
                                                                        @RequestBody @Valid WorkerPatchRequest workerPatchRequest) {
        log.trace(ApiLogMessage.NAME_OF_CURRENT_METHOD.getValue(), ApiUtils.getMethodName());
        IamResponse<WorkerDTO> response = workerService.updatePartiallyWorker(id, workerPatchRequest);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Delete a worker", description = "Deletes a worker by its unique identifier.")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWorker(@Parameter(description = "Worker identifier", example = "42") @PathVariable(name = "id") Integer id) {
        log.trace(ApiLogMessage.NAME_OF_CURRENT_METHOD.getValue(), ApiUtils.getMethodName());
        workerService.deleteWorker(id);
        return ResponseEntity.noContent().build();
    }
}
