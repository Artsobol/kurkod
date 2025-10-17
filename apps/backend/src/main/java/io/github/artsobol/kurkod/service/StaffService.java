package io.github.artsobol.kurkod.service;

import io.github.artsobol.kurkod.model.dto.staff.StaffDTO;
import io.github.artsobol.kurkod.model.request.staff.StaffPatchRequest;
import io.github.artsobol.kurkod.model.request.staff.StaffPostRequest;
import io.github.artsobol.kurkod.model.request.staff.StaffPutRequest;
import io.github.artsobol.kurkod.model.response.IamResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public interface StaffService {

    StaffDTO get(Integer staffId);

    List<StaffDTO> getAll();

    StaffDTO create(StaffPostRequest staffPostRequest);

    StaffDTO replace(Integer staffId, StaffPutRequest staffPutRequest);

    StaffDTO update(Integer staffId, StaffPatchRequest staffPatchRequest);

    void delete(Integer staffId);
}
