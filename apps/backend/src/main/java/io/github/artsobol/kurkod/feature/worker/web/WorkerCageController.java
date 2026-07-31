package io.github.artsobol.kurkod.feature.worker.web;

import io.github.artsobol.kurkod.feature.cage.dto.response.CageResponse;
import io.github.artsobol.kurkod.feature.worker.dto.response.WorkerResponse;
import io.github.artsobol.kurkod.feature.worker.service.WorkerCageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/workers", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@Tag(name = "Worker Cages", description = "Assign or remove cages for workers")
public class WorkerCageController {

    private final WorkerCageService workerCageService;

    @Operation(summary = "Get cages assigned to worker")
    @GetMapping("/{workerId}/cages")
    public ResponseEntity<List<CageResponse>> getWorkerCages(
            @PathVariable Long workerId) {

        List<CageResponse> cages = workerCageService.getWorkerCages(workerId);
        return ResponseEntity.ok(cages);
    }

    @Operation(summary = "Get workers assigned to cage")
    @GetMapping("/cages/{cageId}/workers")
    public ResponseEntity<List<WorkerResponse>> getCageWorkers(
            @PathVariable Long cageId) {

        List<WorkerResponse> workers = workerCageService.getCageWorkers(cageId);
        return ResponseEntity.ok(workers);
    }

    @Operation(summary = "Assign cage to worker")
    @PostMapping("/{workerId}/cages/{cageId}")
    public ResponseEntity<Void> assignCage(
            @PathVariable Long workerId,
            @PathVariable Long cageId) {

        workerCageService.assignCageToWorker(workerId, cageId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(summary = "Unassign cage from worker")
    @DeleteMapping("/{workerId}/cages/{cageId}")
    public ResponseEntity<Void> unassignCage(
            @PathVariable Long workerId,
            @PathVariable Long cageId) {

        workerCageService.unassignCageFromWorker(workerId, cageId);
        return ResponseEntity.noContent().build();
    }
}
