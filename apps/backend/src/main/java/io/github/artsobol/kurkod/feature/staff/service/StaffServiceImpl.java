package io.github.artsobol.kurkod.feature.staff.service;

import static io.github.artsobol.kurkod.infrastructure.utils.VersionUtils.checkVersion;

import io.github.artsobol.kurkod.exception.http.NotFoundException;
import io.github.artsobol.kurkod.feature.staff.dto.request.StaffCreateRequest;
import io.github.artsobol.kurkod.feature.staff.dto.request.StaffUpdateRequest;
import io.github.artsobol.kurkod.feature.staff.dto.response.StaffResponse;
import io.github.artsobol.kurkod.feature.staff.entity.Staff;
import io.github.artsobol.kurkod.feature.staff.mapper.StaffMapper;
import io.github.artsobol.kurkod.feature.staff.repository.StaffRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class StaffServiceImpl implements StaffService {

    private final StaffRepository staffRepository;
    private final StaffMapper staffMapper;


    @Override
    @Transactional
    @PreAuthorize("hasAnyAuthority('DIRECTOR', 'SUPER_ADMIN')")
    public StaffResponse get(Long id) {
        return staffMapper.toResponse(getStaffById(id));
    }

    @Override
    @Transactional
    @PreAuthorize("hasAnyAuthority('DIRECTOR', 'SUPER_ADMIN')")
    public List<StaffResponse> getAll() {
        return staffRepository.findAllByIsActiveTrue().stream()
                .map(staffMapper::toResponse)
                .toList();
    }

    @Override
    public Page<StaffResponse> getAllWithPagination(Pageable pageable) {
        return staffRepository.findAllByIsActiveTrue(pageable).map(staffMapper::toResponse);
    }


    @Override
    @Transactional
    @PreAuthorize("hasAnyAuthority('DIRECTOR', 'SUPER_ADMIN')")
    public StaffResponse create(StaffCreateRequest request) {
        Staff staff = staffMapper.toEntity(request);
        staff = staffRepository.save(staff);
        return staffMapper.toResponse(staff);
    }
    @Override
    @Transactional
    @PreAuthorize("hasAnyAuthority('DIRECTOR', 'SUPER_ADMIN')")
    public StaffResponse update(Long id, StaffUpdateRequest request, Long version) {
        Staff staff = getStaffById(id);
        checkVersion(staff.getVersion(), version);
        staffMapper.updatePartially(staff, request);
        staff = staffRepository.save(staff);
        return staffMapper.toResponse(staff);
    }

    @Override
    @Transactional
    @PreAuthorize("hasAnyAuthority('DIRECTOR', 'SUPER_ADMIN')")
    public void delete(Long id, Long version) {
        Staff staff = getStaffById(id);
        checkVersion(staff.getVersion(), version);
        staff.deactivate();
        staffRepository.save(staff);
    }

    protected Staff getStaffById(Long id) {
        return staffRepository.findStaffByIdAndIsActiveTrue(id).orElseThrow(() ->
                new NotFoundException("staff.not.found", id));
    }
}
