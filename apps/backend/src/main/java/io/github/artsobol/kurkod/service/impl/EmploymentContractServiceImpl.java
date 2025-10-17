package io.github.artsobol.kurkod.service.impl;

import io.github.artsobol.kurkod.mapper.EmploymentContractMapper;
import io.github.artsobol.kurkod.error.impl.EmploymentContractError;
import io.github.artsobol.kurkod.error.impl.StaffError;
import io.github.artsobol.kurkod.error.impl.WorkerError;
import io.github.artsobol.kurkod.model.dto.employmentContract.EmploymentContractDTO;
import io.github.artsobol.kurkod.model.entity.EmploymentContract;
import io.github.artsobol.kurkod.model.entity.Staff;
import io.github.artsobol.kurkod.model.entity.Worker;
import io.github.artsobol.kurkod.model.exception.NotFoundException;
import io.github.artsobol.kurkod.model.request.employmentContract.EmploymentContractPatchRequest;
import io.github.artsobol.kurkod.model.request.employmentContract.EmploymentContractPostRequest;
import io.github.artsobol.kurkod.model.request.employmentContract.EmploymentContractPutRequest;
import io.github.artsobol.kurkod.repository.EmploymentContractRepository;
import io.github.artsobol.kurkod.repository.StaffRepository;
import io.github.artsobol.kurkod.repository.WorkerRepository;
import io.github.artsobol.kurkod.service.EmploymentContractService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class EmploymentContractServiceImpl implements EmploymentContractService {

    private final EmploymentContractRepository employmentContractRepository;
    private final EmploymentContractMapper employmentContractMapper;
    private final WorkerRepository workerRepository;
    private final StaffRepository staffRepository;

    @Override
    @Transactional
    @PreAuthorize("hasAnyAuthority('DIRECTOR', 'SUPER_ADMIN')")
    public EmploymentContractDTO get(Integer workerId) {
        EmploymentContract employmentContract = getContractByWorkerId(workerId);
        return employmentContractMapper.toDto(employmentContract);
    }

    @Override
    @Transactional
    @PreAuthorize("hasAnyAuthority('DIRECTOR', 'SUPER_ADMIN')")
    public EmploymentContractDTO create(Integer workerId,
                                                                       EmploymentContractPostRequest employmentContractPostRequest) {
        Worker worker = workerRepository.findWorkerByIdAndIsActiveTrue(workerId).orElseThrow(
                () -> new NotFoundException(WorkerError.NOT_FOUND_BY_ID.format(workerId))
        );

        Integer staffId = employmentContractPostRequest.getStaffId();
        Staff staff = getStaffByStaffId(staffId);

        EmploymentContract employmentContract = employmentContractMapper.toEntity(employmentContractPostRequest);
        employmentContract.setStaff(staff);
        employmentContract.setWorker(worker);
        employmentContract = employmentContractRepository.save(employmentContract);
        return employmentContractMapper.toDto(employmentContract);
    }

    @Override
    @Transactional
    @PreAuthorize("hasAnyAuthority('DIRECTOR', 'SUPER_ADMIN')")
    public EmploymentContractDTO replace(Integer workerId,
                                                                            EmploymentContractPutRequest employmentContractPutRequest) {
        Integer staffId = employmentContractPutRequest.getStaffId();
        Staff staff = getStaffByStaffId(staffId);

        EmploymentContract employmentContract = getContractByWorkerId(workerId);

        employmentContractMapper.updateFully(employmentContract, employmentContractPutRequest);
        employmentContract.setStaff(staff);
        employmentContract = employmentContractRepository.save(employmentContract);
        return employmentContractMapper.toDto(employmentContract);
    }

    @Override
    @Transactional
    @PreAuthorize("hasAnyAuthority('DIRECTOR', 'SUPER_ADMIN')")
    public EmploymentContractDTO update(Integer workerId,
                                                                                EmploymentContractPatchRequest employmentContractPatchRequest) {
        EmploymentContract employmentContract = getContractByWorkerId(workerId);
        employmentContractMapper.updatePartially(employmentContract, employmentContractPatchRequest);

        Integer staffId = employmentContractPatchRequest.getStaffId();
        if (staffId != null) {
            Staff staff = getStaffByStaffId(staffId);
            employmentContract.setStaff(staff);
        }

        employmentContract = employmentContractRepository.save(employmentContract);
        return employmentContractMapper.toDto(employmentContract);
    }

    @Override
    @Transactional
    @PreAuthorize("hasAnyAuthority('DIRECTOR', 'SUPER_ADMIN')")
    public void delete(Integer workerId) {
        EmploymentContract employmentContract = getContractByWorkerId(workerId);
        employmentContract.setActive(false);
        employmentContractRepository.save(employmentContract);
    }

    protected EmploymentContract getContractByWorkerId(Integer workerId) {
        return employmentContractRepository.findEmploymentContractByWorkerIdAndIsActiveTrue(workerId).orElseThrow(
                () -> new NotFoundException(EmploymentContractError.NOT_FOUND_BY_ID.format(workerId))
        );
    }

    protected Staff getStaffByStaffId(Integer staffId) {
        return staffRepository.findStaffByIdAndIsActiveTrue(staffId).orElseThrow(
                () -> new NotFoundException(StaffError.NOT_FOUND_BY_ID.format(staffId))
        );
    }
}
