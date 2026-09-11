package io.github.artsobol.kurkod.feature.staff.service;

import io.github.artsobol.kurkod.feature.staff.dto.request.StaffCreateRequest;
import io.github.artsobol.kurkod.feature.staff.dto.request.StaffUpdateRequest;
import io.github.artsobol.kurkod.feature.staff.dto.response.StaffResponse;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface StaffService {

  StaffResponse get(Long id);

  List<StaffResponse> getAll();

  Page<StaffResponse> getAllWithPagination(Pageable pageable);

  StaffResponse create(StaffCreateRequest request);

  StaffResponse update(Long id, StaffUpdateRequest request, Long version);

  void delete(Long id, Long version);
}
