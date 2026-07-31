package io.github.artsobol.kurkod.feature.worker.service;

import io.github.artsobol.kurkod.feature.worker.dto.response.WorkerDTO;
import io.github.artsobol.kurkod.feature.worker.dto.request.WorkerUpdateRequest;
import io.github.artsobol.kurkod.feature.worker.dto.request.WorkerCreateRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface WorkerService {

    WorkerDTO get(Long id);

    List<WorkerDTO> getAll();

    Page<WorkerDTO> getPage(Pageable pageable);

    WorkerDTO create(WorkerCreateRequest request);
    WorkerDTO update(Long id, WorkerUpdateRequest request, Long version);

    void delete(Long id, Long version);
}
