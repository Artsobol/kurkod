package io.github.artsobol.kurkod.service.impl;

import io.github.artsobol.kurkod.mapper.StaffMapper;
import io.github.artsobol.kurkod.error.impl.StaffError;
import io.github.artsobol.kurkod.model.dto.staff.StaffDTO;
import io.github.artsobol.kurkod.model.entity.Staff;
import io.github.artsobol.kurkod.model.exception.NotFoundException;
import io.github.artsobol.kurkod.model.request.staff.StaffPatchRequest;
import io.github.artsobol.kurkod.model.request.staff.StaffPostRequest;
import io.github.artsobol.kurkod.model.request.staff.StaffPutRequest;
import io.github.artsobol.kurkod.repository.StaffRepository;
import io.github.artsobol.kurkod.service.StaffService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class StaffServiceImpl implements StaffService {

    private final StaffRepository staffRepository;
    private final StaffMapper staffMapper;

    @Override
    @Transactional
    @PreAuthorize("hasAnyAuthority('DIRECTOR', 'SUPER_ADMIN')")
    public StaffDTO get(Integer staffId) {
        Staff staff = getStaffById(staffId);
        return staffMapper.toDto(staff);
    }

    @Override
    @Transactional
    @PreAuthorize("hasAnyAuthority('DIRECTOR', 'SUPER_ADMIN')")
    public List<StaffDTO> getAll() {
        return staffRepository.findAllByIsActiveTrue().stream()
                .map(staffMapper::toDto)
                .toList();
    }


    @Override
    @Transactional
    @PreAuthorize("hasAnyAuthority('DIRECTOR', 'SUPER_ADMIN')")
    public StaffDTO create(StaffPostRequest staffPostRequest) {
        Staff staff = staffMapper.toEntity(staffPostRequest);
        staff = staffRepository.save(staff);
        return staffMapper.toDto(staff);
    }

    @Override
    @Transactional
    @PreAuthorize("hasAnyAuthority('DIRECTOR', 'SUPER_ADMIN')")
    public StaffDTO replace(Integer staffId, StaffPutRequest staffPutRequest) {
        Staff staff = getStaffById(staffId);
        staffMapper.updateFully(staff, staffPutRequest);
        staff = staffRepository.save(staff);
        return staffMapper.toDto(staff);
    }

    @Override
    @Transactional
    @PreAuthorize("hasAnyAuthority('DIRECTOR', 'SUPER_ADMIN')")
    public StaffDTO update(Integer staffId, StaffPatchRequest staffPatchRequest) {
        Staff staff = getStaffById(staffId);
        staffMapper.updatePartially(staff, staffPatchRequest);
        staff = staffRepository.save(staff);
        return staffMapper.toDto(staff);
    }

    @Override
    @Transactional
    @PreAuthorize("hasAnyAuthority('DIRECTOR', 'SUPER_ADMIN')")
    public void delete(Integer staffId) {
        Staff staff = getStaffById(staffId);
        staff.setActive(false);
        staffRepository.save(staff);
    }

    protected Staff getStaffById(Integer id) {
        return staffRepository.findStaffByIdAndIsActiveTrue(id).orElseThrow(() ->
                new NotFoundException(StaffError.NOT_FOUND_BY_ID.format(id)));
    }
}
