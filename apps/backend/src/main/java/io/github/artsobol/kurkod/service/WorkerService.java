package io.github.artsobol.kurkod.service;

import io.github.artsobol.kurkod.model.dto.worker.WorkerDTO;
import io.github.artsobol.kurkod.model.request.worker.WorkerPatchRequest;
import io.github.artsobol.kurkod.model.request.worker.WorkerPostRequest;
import io.github.artsobol.kurkod.model.request.worker.WorkerPutRequest;
import io.github.artsobol.kurkod.model.response.IamResponse;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public interface WorkerService {

    IamResponse<WorkerDTO> getWorkerById(@NotNull Integer id);

    IamResponse<List<WorkerDTO>> getAllWorkers();

    IamResponse<WorkerDTO> createWorker(WorkerPostRequest workerPostRequest);

    IamResponse<WorkerDTO> updateFullyWorker(@NotNull Integer id, WorkerPutRequest workerPutRequest);

    IamResponse<WorkerDTO> updatePartiallyWorker(@NotNull Integer id, WorkerPatchRequest workerPatchRequest);

    void deleteWorker(@NotNull Integer id);
}
