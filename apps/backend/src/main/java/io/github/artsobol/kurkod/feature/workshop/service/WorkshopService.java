package io.github.artsobol.kurkod.feature.workshop.service;

import io.github.artsobol.kurkod.feature.workshop.dto.response.WorkshopResponse;
import io.github.artsobol.kurkod.feature.workshop.dto.request.WorkshopUpdateRequest;
import io.github.artsobol.kurkod.feature.workshop.dto.request.WorkshopCreateRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface WorkshopService {

  WorkshopResponse get(Long id);

  List<WorkshopResponse> getAll();

  Page<WorkshopResponse> getAllWithPagination(Pageable pageable);

  WorkshopResponse create(WorkshopCreateRequest request);

  WorkshopResponse update(Long id, WorkshopUpdateRequest request, Long version);

  void delete(Long id, Long version);
}
