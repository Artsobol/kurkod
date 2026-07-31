package io.github.artsobol.kurkod.feature.worker.service;

import io.github.artsobol.kurkod.feature.cage.dto.response.CageResponse;
import io.github.artsobol.kurkod.feature.worker.dto.response.WorkerResponse;

import java.util.List;

public interface WorkerCageService {

    List<CageResponse> getWorkerCages(Long workerId);

    List<WorkerResponse> getCageWorkers(Long cageId);

    void assignCageToWorker(Long workerId, Long cageId);

    void unassignCageFromWorker(Long workerId, Long cageId);

    boolean hasWorkerAnyCages(Long workerId);

    boolean isCageServedByAnyWorker(Long cageId);
}
