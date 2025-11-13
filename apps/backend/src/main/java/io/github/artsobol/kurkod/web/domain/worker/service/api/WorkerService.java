package io.github.artsobol.kurkod.web.domain.worker.service.api;

import io.github.artsobol.kurkod.web.domain.worker.model.dto.WorkerDTO;
import io.github.artsobol.kurkod.web.domain.worker.model.request.WorkerPatchRequest;
import io.github.artsobol.kurkod.web.domain.worker.model.request.WorkerPostRequest;
import io.github.artsobol.kurkod.web.domain.worker.model.request.WorkerPutRequest;

import java.util.List;

public interface WorkerService {

    WorkerDTO get(Integer id);

    List<WorkerDTO> getAll();

    WorkerDTO create(WorkerPostRequest request);

    WorkerDTO replace(Integer id, WorkerPutRequest request, Long version);

    WorkerDTO update(Integer id, WorkerPatchRequest request, Long version);

    void delete(Integer id, Long version);
}
