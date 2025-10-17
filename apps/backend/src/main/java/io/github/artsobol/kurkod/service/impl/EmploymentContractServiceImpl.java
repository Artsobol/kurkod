package io.github.artsobol.kurkod.service.impl;

import io.github.artsobol.kurkod.mapper.EmploymentContractMapper;
import io.github.artsobol.kurkod.model.dto.employmentContract.EmploymentContractDTO;
import io.github.artsobol.kurkod.model.entity.EmploymentContract;
import io.github.artsobol.kurkod.model.entity.Staff;
import io.github.artsobol.kurkod.model.entity.Worker;
import io.github.artsobol.kurkod.model.exception.NotFoundException;
import io.github.artsobol.kurkod.model.request.employmentContract.EmploymentContractPatchRequest;
import io.github.artsobol.kurkod.model.request.employmentContract.EmploymentContractPostRequest;
import io.github.artsobol.kurkod.model.request.employmentContract.EmploymentContractPutRequest;
import io.github.artsobol.kurkod.model.response.IamResponse;
import io.github.artsobol.kurkod.repository.EmploymentContractRepository;
import io.github.artsobol.kurkod.repository.StaffRepository;
import io.github.artsobol.kurkod.repository.WorkerRepository;
import io.github.artsobol.kurkod.security.validation.AccessValidator;
import io.github.artsobol.kurkod.service.EmploymentContractService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmploymentContractServiceImpl implements EmploymentContractService {

    private final EmploymentContractRepository employmentContractRepository;
    private final EmploymentContractMapper employmentContractMapper;
    private final WorkerRepository workerRepository;
    private final StaffRepository staffRepository;
    private final AccessValidator accessValidator;

    @Override
    public IamResponse<EmploymentContractDTO> getByWorkerId(Integer workerId) {
        accessValidator.validateDirectorOrSuperAdmin();

        EmploymentContract employmentContract = getContractByWorkerId(workerId);
        return IamResponse.createSuccessful(employmentContractMapper.toDto(employmentContract));
    }

    @Override
    public IamResponse<EmploymentContractDTO> createEmploymentContract(Integer workerId, EmploymentContractPostRequest employmentContractPostRequest) {
        accessValidator.validateDirectorOrSuperAdmin();

        Worker worker = workerRepository.findWorkerByIdAndIsActiveTrue(workerId).orElseThrow(
                () -> new NotFoundException("Worker with id " + workerId + " not found")
        );

        Integer staffId = employmentContractPostRequest.getStaffId();
        Staff staff = getStaffByStaffId(staffId);

        EmploymentContract employmentContract = employmentContractMapper.toEntity(employmentContractPostRequest);
        employmentContract.setStaff(staff);
        employmentContract.setWorker(worker);
        employmentContract = employmentContractRepository.save(employmentContract);
        return IamResponse.createSuccessful(employmentContractMapper.toDto(employmentContract));
    }

    @Override
    public IamResponse<EmploymentContractDTO> updateFullyEmploymentContract(Integer workerId, EmploymentContractPutRequest employmentContractPutRequest) {
        accessValidator.validateDirectorOrSuperAdmin();

        Integer staffId = employmentContractPutRequest.getStaffId();
        Staff staff = getStaffByStaffId(staffId);

        EmploymentContract employmentContract = getContractByWorkerId(workerId);

        employmentContractMapper.updateFully(employmentContract, employmentContractPutRequest);
        employmentContract.setStaff(staff);
        employmentContract = employmentContractRepository.save(employmentContract);
        return IamResponse.createSuccessful(employmentContractMapper.toDto(employmentContract));
    }

    @Override
    public IamResponse<EmploymentContractDTO> updatePartiallyEmploymentContract(Integer workerId, EmploymentContractPatchRequest employmentContractPatchRequest) {
        accessValidator.validateDirectorOrSuperAdmin();

        EmploymentContract employmentContract = getContractByWorkerId(workerId);

        employmentContractMapper.updatePartially(employmentContract, employmentContractPatchRequest);


        Integer staffId = employmentContractPatchRequest.getStaffId();
        if (staffId != null) {
            Staff staff = getStaffByStaffId(staffId);
            employmentContract.setStaff(staff);
        }

        employmentContract = employmentContractRepository.save(employmentContract);
        return IamResponse.createSuccessful(employmentContractMapper.toDto(employmentContract));
    }

    @Override
    public void deleteEmploymentContract(Integer workerId) {
        accessValidator.validateDirectorOrSuperAdmin();

        EmploymentContract employmentContract = getContractByWorkerId(workerId);
        employmentContract.setActive(false);
        employmentContractRepository.save(employmentContract);
    }

    protected EmploymentContract getContractByWorkerId(Integer workerId) {
        return employmentContractRepository.findEmploymentContractByWorkerIdAndIsActiveTrue(workerId).orElseThrow(
                () -> new NotFoundException("Employment contract not found for worker with id " + workerId)
        );
    }

    protected Staff getStaffByStaffId(Integer staffId) {
        return staffRepository.findStaffByIdAndIsActiveTrue(staffId).orElseThrow(
                () -> new NotFoundException("Staff with id " + staffId + " not found")
        );
    }
}
