package io.github.artsobol.kurkod.web.domain.worker.service.api;

import io.github.artsobol.kurkod.web.domain.worker.model.dto.WorkerDTO;
import io.github.artsobol.kurkod.web.domain.worker.model.request.WorkerPatchRequest;
import io.github.artsobol.kurkod.web.domain.worker.model.request.WorkerPostRequest;
import io.github.artsobol.kurkod.web.domain.worker.model.request.WorkerPutRequest;

import java.util.List;

public interface WorkerService {

    WorkerDTO get(Integer id);

    List<WorkerDTO> getAll();

    WorkerDTO create(WorkerPostRequest workerPostRequest);

    WorkerDTO replace(Integer id, WorkerPutRequest workerPutRequest);

    WorkerDTO update(Integer id, WorkerPatchRequest workerPatchRequest);

    void delete(Integer id);
}
