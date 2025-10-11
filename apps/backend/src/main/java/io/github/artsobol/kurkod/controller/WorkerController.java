package io.github.artsobol.kurkod.controller;

import io.github.artsobol.kurkod.model.dto.worker.WorkerDTO;
import io.github.artsobol.kurkod.model.request.worker.WorkerPatchRequest;
import io.github.artsobol.kurkod.model.request.worker.WorkerPostRequest;
import io.github.artsobol.kurkod.model.request.worker.WorkerPutRequest;
import io.github.artsobol.kurkod.model.response.IamResponse;
import io.github.artsobol.kurkod.service.WorkerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/workers")
@RequiredArgsConstructor
public class WorkerController {

    private final WorkerService workerService;

    @GetMapping
    public ResponseEntity<IamResponse<List<WorkerDTO>>> getAllWorkers()
    {
        IamResponse<List<WorkerDTO>> response = workerService.getAllWorkers();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<IamResponse<WorkerDTO>> getWorkerById(@PathVariable(name = "id") Integer id){
        IamResponse<WorkerDTO> response = workerService.getWorkerById(id);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<IamResponse<WorkerDTO>> createWorker(@RequestBody @Valid WorkerPostRequest workerPostRequest) {
        IamResponse<WorkerDTO> response = workerService.createWorker(workerPostRequest);
        return ResponseEntity.status(201).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<IamResponse<WorkerDTO>> updateFullyWorker(@PathVariable(name = "id") Integer id, @RequestBody @Valid WorkerPutRequest workerPutRequest) {
        IamResponse<WorkerDTO> response = workerService.updateFullyWorker(id, workerPutRequest);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<IamResponse<WorkerDTO>> updatePartiallyWorker(@PathVariable(name = "id") Integer id, @RequestBody @Valid WorkerPatchRequest workerPatchRequest) {
        IamResponse<WorkerDTO> response = workerService.updatePartiallyWorker(id, workerPatchRequest);
        return ResponseEntity.ok(response);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWorker(@PathVariable(name = "id") Integer id) {
        workerService.deleteWorker(id);
        return ResponseEntity.noContent().build();
    }
}
