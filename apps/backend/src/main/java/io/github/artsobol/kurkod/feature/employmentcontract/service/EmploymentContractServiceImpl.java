package io.github.artsobol.kurkod.feature.employmentcontract.service;

import io.github.artsobol.kurkod.feature.employmentcontract.mapper.EmploymentContractMapper;
import io.github.artsobol.kurkod.feature.employmentcontract.dto.response.EmploymentContractDTO;
import io.github.artsobol.kurkod.feature.employmentcontract.entity.EmploymentContract;
import io.github.artsobol.kurkod.feature.staff.entity.Staff;
import io.github.artsobol.kurkod.feature.worker.entity.Worker;
import io.github.artsobol.kurkod.exception.http.NotFoundException;
import io.github.artsobol.kurkod.feature.employmentcontract.dto.request.EmploymentContractUpdateRequest;
import io.github.artsobol.kurkod.feature.employmentcontract.dto.request.EmploymentContractCreateRequest;
import io.github.artsobol.kurkod.feature.employmentcontract.repository.EmploymentContractRepository;
import io.github.artsobol.kurkod.feature.staff.repository.StaffRepository;
import io.github.artsobol.kurkod.feature.worker.repository.WorkerRepository;
import io.github.artsobol.kurkod.feature.employmentcontract.service.EmploymentContractService;
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
    public EmploymentContractDTO get(Long workerId) {
        return employmentContractMapper.toDto(getContractByWorkerId(workerId));
    }

    @Override
    @Transactional
    @PreAuthorize("hasAnyAuthority('DIRECTOR', 'SUPER_ADMIN')")
    public EmploymentContractDTO create(Long workerId, EmploymentContractCreateRequest request) {
        Worker worker = workerRepository.findWorkerByIdAndIsActiveTrue(workerId)
                                        .orElseThrow(() -> new NotFoundException("worker.not.found", workerId));

        Long staffId = request.getStaffId();
        Staff staff = getStaffByStaffId(staffId);

        EmploymentContract employmentContract = employmentContractMapper.toEntity(request);
        employmentContract.setStaff(staff);
        employmentContract.setWorker(worker);
        employmentContract = employmentContractRepository.save(employmentContract);
        return employmentContractMapper.toDto(employmentContract);
    }
    @Override
    @Transactional
    @PreAuthorize("hasAnyAuthority('DIRECTOR', 'SUPER_ADMIN')")
    public EmploymentContractDTO update(
            Long workerId,
            EmploymentContractUpdateRequest request,
            Long expectedVersion) {
        EmploymentContract employmentContract = getContractByWorkerId(workerId);
        employmentContractMapper.updatePartially(employmentContract, request);

        Long staffId = request.getStaffId();
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
    public void delete(Long workerId, Long expectedVersion) {
        EmploymentContract employmentContract = getContractByWorkerId(workerId);
        employmentContract.setActive(false);
        employmentContractRepository.save(employmentContract);
    }

    protected EmploymentContract getContractByWorkerId(Long workerId) {
        return employmentContractRepository.findEmploymentContractByWorkerIdAndIsActiveTrue(workerId)
                                           .orElseThrow(() -> new NotFoundException("employment.contract.not.found.by.worker", workerId));
    }

    protected Staff getStaffByStaffId(Long staffId) {
        return staffRepository.findStaffByIdAndIsActiveTrue(staffId)
                              .orElseThrow(() -> new NotFoundException("staff.not.found", staffId));
    }
}
