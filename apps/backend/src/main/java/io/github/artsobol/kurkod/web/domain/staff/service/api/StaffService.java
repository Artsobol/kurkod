package io.github.artsobol.kurkod.web.domain.staff.service.api;

import io.github.artsobol.kurkod.web.domain.staff.model.dto.StaffDTO;
import io.github.artsobol.kurkod.web.domain.staff.model.request.StaffPatchRequest;
import io.github.artsobol.kurkod.web.domain.staff.model.request.StaffPostRequest;
import io.github.artsobol.kurkod.web.domain.staff.model.request.StaffPutRequest;

import java.util.List;

public interface StaffService {

    StaffDTO get(Integer staffId);

    List<StaffDTO> getAll();

    StaffDTO create(StaffPostRequest staffPostRequest);

    StaffDTO replace(Integer staffId, StaffPutRequest staffPutRequest);

    StaffDTO update(Integer staffId, StaffPatchRequest staffPatchRequest);

    void delete(Integer staffId);
}
