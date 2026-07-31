package io.github.artsobol.kurkod.feature.workshop.service;

import io.github.artsobol.kurkod.feature.workshop.dto.response.WorkshopDTO;
import io.github.artsobol.kurkod.feature.workshop.dto.request.WorkshopUpdateRequest;
import io.github.artsobol.kurkod.feature.workshop.dto.request.WorkshopCreateRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface WorkshopService {

    WorkshopDTO get(Long id);

    List<WorkshopDTO> getAll();

    Page<WorkshopDTO> getAllWithPagination(Pageable pageable);

    WorkshopDTO create(WorkshopCreateRequest request);

    WorkshopDTO update(Long id, WorkshopUpdateRequest request, Long version);
    void delete(Long id, Long version);
}
