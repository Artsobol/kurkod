package io.github.artsobol.kurkod.service;

import io.github.artsobol.kurkod.model.dto.worker.WorkerDTO;
import io.github.artsobol.kurkod.model.request.worker.WorkerPatchRequest;
import io.github.artsobol.kurkod.model.request.worker.WorkerPostRequest;
import io.github.artsobol.kurkod.model.request.worker.WorkerPutRequest;
import io.github.artsobol.kurkod.model.response.IamResponse;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public interface WorkerService {

    WorkerDTO get(Integer id);

    List<WorkerDTO> getAll();

    WorkerDTO create(WorkerPostRequest workerPostRequest);

    WorkerDTO replace(Integer id, WorkerPutRequest workerPutRequest);

    WorkerDTO update(Integer id, WorkerPatchRequest workerPatchRequest);

    void delete(Integer id);
}
