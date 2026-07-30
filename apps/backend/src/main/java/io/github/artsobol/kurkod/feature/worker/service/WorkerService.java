package io.github.artsobol.kurkod.feature.worker.service;

import io.github.artsobol.kurkod.feature.worker.dto.response.WorkerDTO;
import io.github.artsobol.kurkod.feature.worker.dto.request.WorkerPatchRequest;
import io.github.artsobol.kurkod.feature.worker.dto.request.WorkerPostRequest;
import io.github.artsobol.kurkod.feature.worker.dto.request.WorkerPutRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface WorkerService {

    WorkerDTO get(Long id);

    List<WorkerDTO> getAll();

    Page<WorkerDTO> getPage(Pageable pageable);

    WorkerDTO create(WorkerPostRequest request);

    WorkerDTO replace(Long id, WorkerPutRequest request, Long version);

    WorkerDTO update(Long id, WorkerPatchRequest request, Long version);

    void delete(Long id, Long version);
}
