package io.github.artsobol.kurkod.web.domain.staff.service.api;

import io.github.artsobol.kurkod.web.domain.staff.model.dto.StaffDTO;
import io.github.artsobol.kurkod.web.domain.staff.model.request.StaffPatchRequest;
import io.github.artsobol.kurkod.web.domain.staff.model.request.StaffPostRequest;
import io.github.artsobol.kurkod.web.domain.staff.model.request.StaffPutRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface StaffService {

    StaffDTO get(Long id);

    List<StaffDTO> getAll();

    Page<StaffDTO> getAllWithPagination(Pageable pageable);

    StaffDTO create(StaffPostRequest request);

    StaffDTO replace(Long id, StaffPutRequest request, Long version);

    StaffDTO update(Long id, StaffPatchRequest request, Long version);

    void delete(Long id, Long version);
}
