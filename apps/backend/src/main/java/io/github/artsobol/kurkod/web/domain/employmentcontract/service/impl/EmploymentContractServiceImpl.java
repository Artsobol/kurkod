package io.github.artsobol.kurkod.web.domain.employmentcontract.service.impl;

import io.github.artsobol.kurkod.common.constants.ApiLogMessage;
import io.github.artsobol.kurkod.common.logging.LogHelper;
import io.github.artsobol.kurkod.security.facade.SecurityContextFacade;
import io.github.artsobol.kurkod.web.domain.employmentcontract.mapper.EmploymentContractMapper;
import io.github.artsobol.kurkod.web.domain.employmentcontract.error.EmploymentContractError;
import io.github.artsobol.kurkod.web.domain.staff.error.StaffError;
import io.github.artsobol.kurkod.web.domain.worker.error.WorkerError;
import io.github.artsobol.kurkod.web.domain.employmentcontract.model.dto.EmploymentContractDTO;
import io.github.artsobol.kurkod.web.domain.employmentcontract.model.entity.EmploymentContract;
import io.github.artsobol.kurkod.web.domain.staff.model.entity.Staff;
import io.github.artsobol.kurkod.web.domain.worker.model.entity.Worker;
import io.github.artsobol.kurkod.common.exception.NotFoundException;
import io.github.artsobol.kurkod.web.domain.employmentcontract.model.request.EmploymentContractPatchRequest;
import io.github.artsobol.kurkod.web.domain.employmentcontract.model.request.EmploymentContractPostRequest;
import io.github.artsobol.kurkod.web.domain.employmentcontract.model.request.EmploymentContractPutRequest;
import io.github.artsobol.kurkod.web.domain.employmentcontract.repository.EmploymentContractRepository;
import io.github.artsobol.kurkod.web.domain.staff.repository.StaffRepository;
import io.github.artsobol.kurkod.web.domain.worker.repository.WorkerRepository;
import io.github.artsobol.kurkod.web.domain.employmentcontract.service.api.EmploymentContractService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class EmploymentContractServiceImpl implements EmploymentContractService {

    private final EmploymentContractRepository employmentContractRepository;
    private final EmploymentContractMapper employmentContractMapper;
    private final WorkerRepository workerRepository;
    private final StaffRepository staffRepository;
    private final SecurityContextFacade securityContextFacade;

    private String getCurrentUsername() {
        return securityContextFacade.getCurrentUsername();
    }

    @Override
    @Transactional
    @PreAuthorize("hasAnyAuthority('DIRECTOR', 'SUPER_ADMIN')")
    public EmploymentContractDTO get(Integer workerId) {
        log.debug(ApiLogMessage.GET_ENTITY.getValue(), getCurrentUsername(), LogHelper.getEntityName(EmploymentContract.class), workerId);
        return employmentContractMapper.toDto(getContractByWorkerId(workerId));
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
        log.info(ApiLogMessage.CREATE_ENTITY.getValue(), getCurrentUsername(), LogHelper.getEntityName(EmploymentContract.class), workerId);
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
        log.info(ApiLogMessage.REPLACE_ENTITY.getValue(), getCurrentUsername(), LogHelper.getEntityName(EmploymentContract.class), workerId);
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
        log.info(ApiLogMessage.UPDATE_ENTITY.getValue(), getCurrentUsername(), LogHelper.getEntityName(EmploymentContract.class), workerId);
        return employmentContractMapper.toDto(employmentContract);
    }

    @Override
    @Transactional
    @PreAuthorize("hasAnyAuthority('DIRECTOR', 'SUPER_ADMIN')")
    public void delete(Integer workerId) {
        EmploymentContract employmentContract = getContractByWorkerId(workerId);
        employmentContract.setActive(false);
        log.info(ApiLogMessage.DELETE_ENTITY.getValue(), getCurrentUsername(), LogHelper.getEntityName(EmploymentContract.class), workerId);
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
