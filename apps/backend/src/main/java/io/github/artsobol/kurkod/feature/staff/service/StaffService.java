package io.github.artsobol.kurkod.feature.staff.service;

import io.github.artsobol.kurkod.feature.staff.dto.response.StaffDTO;
import io.github.artsobol.kurkod.feature.staff.dto.request.StaffUpdateRequest;
import io.github.artsobol.kurkod.feature.staff.dto.request.StaffCreateRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface StaffService {

    StaffDTO get(Long id);

    List<StaffDTO> getAll();

    Page<StaffDTO> getAllWithPagination(Pageable pageable);

    StaffDTO create(StaffCreateRequest request);
    StaffDTO update(Long id, StaffUpdateRequest request, Long version);

    void delete(Long id, Long version);
}
