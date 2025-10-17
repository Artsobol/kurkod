package io.github.artsobol.kurkod.service.impl;

import io.github.artsobol.kurkod.mapper.StaffMapper;
import io.github.artsobol.kurkod.model.dto.staff.StaffDTO;
import io.github.artsobol.kurkod.model.entity.Staff;
import io.github.artsobol.kurkod.model.exception.NotFoundException;
import io.github.artsobol.kurkod.model.request.staff.StaffPatchRequest;
import io.github.artsobol.kurkod.model.request.staff.StaffPostRequest;
import io.github.artsobol.kurkod.model.request.staff.StaffPutRequest;
import io.github.artsobol.kurkod.model.response.IamResponse;
import io.github.artsobol.kurkod.repository.StaffRepository;
import io.github.artsobol.kurkod.security.validation.AccessValidator;
import io.github.artsobol.kurkod.service.StaffService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StaffServiceImpl implements StaffService {

    private final StaffRepository staffRepository;
    private final StaffMapper staffMapper;
    private final AccessValidator accessValidator;

    @Override
    public IamResponse<StaffDTO> getStaffInfo(Integer staffId) {
        accessValidator.validateDirectorOrSuperAdmin();

        Staff staff = staffRepository.findStaffByIdAndIsActiveTrue(staffId).orElseThrow(
                () -> new NotFoundException("Staff with id " + staffId + " not found")
        );
        return IamResponse.createSuccessful(staffMapper.toDto(staff));
    }

    @Override
    public IamResponse<List<StaffDTO>> getAllStaffs() {
        accessValidator.validateDirectorOrSuperAdmin();

        List<StaffDTO> staffs = staffRepository.findAllByIsActiveTrue().stream()
                .map(staffMapper::toDto)
                .toList();
        return IamResponse.createSuccessful(staffs);
    }


    @Override
    public IamResponse<StaffDTO> createStaff(StaffPostRequest staffPostRequest) {
        accessValidator.validateDirectorOrSuperAdmin();

        Staff staff = staffMapper.toEntity(staffPostRequest);
        staff = staffRepository.save(staff);
        return IamResponse.createSuccessful(staffMapper.toDto(staff));
    }

    @Override
    public IamResponse<StaffDTO> updateFullyStaff(Integer staffId, StaffPutRequest staffPutRequest) {
        accessValidator.validateDirectorOrSuperAdmin();

        Staff staff = staffRepository.findStaffByIdAndIsActiveTrue(staffId).orElseThrow(
                () -> new NotFoundException("Staff with id " + staffId + " not found")
        );
        staffMapper.updateFully(staff, staffPutRequest);
        staff = staffRepository.save(staff);
        return IamResponse.createSuccessful(staffMapper.toDto(staff));
    }

    @Override
    public IamResponse<StaffDTO> updatePartiallyStaff(Integer staffId, StaffPatchRequest staffPatchRequest) {
        accessValidator.validateDirectorOrSuperAdmin();

        Staff staff = staffRepository.findStaffByIdAndIsActiveTrue(staffId).orElseThrow(
                () -> new NotFoundException("Staff with id " + staffId + " not found")
        );
        staffMapper.updatePartially(staff, staffPatchRequest);
        staff = staffRepository.save(staff);
        return IamResponse.createSuccessful(staffMapper.toDto(staff));
    }

    @Override
    public void deleteStaff(Integer staffId) {
        accessValidator.validateDirectorOrSuperAdmin();

        Staff staff = staffRepository.findStaffByIdAndIsActiveTrue(staffId).orElseThrow(
                () -> new NotFoundException("Staff with id " + staffId + " not found")
        );
        staff.setActive(false);
        staffRepository.save(staff);
    }
}
