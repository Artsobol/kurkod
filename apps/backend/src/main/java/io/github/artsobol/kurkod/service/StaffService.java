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

    IamResponse<StaffDTO>  getStaffInfo(@NotNull Integer staffId);

    IamResponse<List<StaffDTO>> getAllStaffs();

    IamResponse<StaffDTO> createStaff(StaffPostRequest staffPostRequest);

    IamResponse<StaffDTO> updateFullyStaff(@NotNull Integer staffId, @Valid  @RequestBody StaffPutRequest staffPutRequest);

    IamResponse<StaffDTO> updatePartiallyStaff(@NotNull Integer staffId, @RequestBody StaffPatchRequest staffPatchRequest);

    void deleteStaff(@NotNull Integer staffId);
}
