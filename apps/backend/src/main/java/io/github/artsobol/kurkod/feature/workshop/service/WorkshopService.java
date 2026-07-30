package io.github.artsobol.kurkod.feature.workshop.service;

import io.github.artsobol.kurkod.feature.workshop.dto.response.WorkshopDTO;
import io.github.artsobol.kurkod.feature.workshop.dto.request.WorkshopPatchRequest;
import io.github.artsobol.kurkod.feature.workshop.dto.request.WorkshopPostRequest;
import io.github.artsobol.kurkod.feature.workshop.dto.request.WorkshopPutRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface WorkshopService {

    WorkshopDTO get(Long id);

    List<WorkshopDTO> getAll();

    Page<WorkshopDTO> getAllWithPagination(Pageable pageable);

    WorkshopDTO create(WorkshopPostRequest request);

    WorkshopDTO update(Long id, WorkshopPatchRequest request, Long version);

    WorkshopDTO replace(Long id, WorkshopPutRequest request, Long version);

    void delete(Long id, Long version);
}
