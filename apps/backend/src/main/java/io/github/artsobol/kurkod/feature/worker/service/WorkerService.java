package io.github.artsobol.kurkod.feature.worker.service;

import io.github.artsobol.kurkod.feature.worker.dto.response.WorkerResponse;
import io.github.artsobol.kurkod.feature.worker.dto.request.WorkerUpdateRequest;
import io.github.artsobol.kurkod.feature.worker.dto.request.WorkerCreateRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface WorkerService {

    WorkerResponse get(Long id);

    List<WorkerResponse> getAll();

    Page<WorkerResponse> getPage(Pageable pageable);

    WorkerResponse create(WorkerCreateRequest request);
    WorkerResponse update(Long id, WorkerUpdateRequest request, Long version);

    void delete(Long id, Long version);
}
