package io.github.artsobol.kurkod.feature.worker.service;

import io.github.artsobol.kurkod.feature.cage.dto.response.CageDTO;
import io.github.artsobol.kurkod.feature.worker.dto.response.WorkerDTO;

import java.util.List;

public interface WorkerCageService {

    List<CageDTO> getWorkerCages(Long workerId);

    List<WorkerDTO> getCageWorkers(Long cageId);

    void assignCageToWorker(Long workerId, Long cageId);

    void unassignCageFromWorker(Long workerId, Long cageId);

    boolean hasWorkerAnyCages(Long workerId);

    boolean isCageServedByAnyWorker(Long cageId);
}
