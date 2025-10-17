package io.github.artsobol.kurkod.service.impl;

import io.github.artsobol.kurkod.mapper.WorkerMapper;
import io.github.artsobol.kurkod.model.dto.worker.WorkerDTO;
import io.github.artsobol.kurkod.model.entity.Worker;
import io.github.artsobol.kurkod.model.exception.NotFoundException;
import io.github.artsobol.kurkod.model.request.worker.WorkerPatchRequest;
import io.github.artsobol.kurkod.model.request.worker.WorkerPostRequest;
import io.github.artsobol.kurkod.model.request.worker.WorkerPutRequest;
import io.github.artsobol.kurkod.model.response.IamResponse;
import io.github.artsobol.kurkod.repository.WorkerRepository;
import io.github.artsobol.kurkod.security.validation.AccessValidator;
import io.github.artsobol.kurkod.service.WorkerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class WorkerServiceImpl implements WorkerService {

    private final WorkerRepository workerRepository;
    private final WorkerMapper workerMapper;
    private final AccessValidator accessValidator;

    @Override
    public IamResponse<WorkerDTO> getWorkerById(Integer id) {
        accessValidator.validateDirectorOrSuperAdmin();

        Worker worker = workerRepository.findWorkerByIdAndIsActiveTrue(id).orElseThrow(
                () -> new NotFoundException("Worker with id " + id + " not found")
        );
        return IamResponse.createSuccessful(workerMapper.toDTO(worker));
    }

    @Override
    public IamResponse<List<WorkerDTO>> getAllWorkers() {
        accessValidator.validateDirectorOrSuperAdmin();

        List<WorkerDTO> workers = workerRepository.findAllByIsActiveTrue().stream().map(workerMapper::toDTO).toList();
        return IamResponse.createSuccessful(workers);
    }

    @Override
    public IamResponse<WorkerDTO> createWorker(WorkerPostRequest workerPostRequest) {
        accessValidator.validateDirectorOrSuperAdmin();

        Worker worker = workerMapper.toEntity(workerPostRequest);
        worker = workerRepository.save(worker);
        return IamResponse.createSuccessful(workerMapper.toDTO(worker));
    }

    @Override
    public IamResponse<WorkerDTO> updateFullyWorker(Integer id, WorkerPutRequest workerPutRequest) {
        accessValidator.validateDirectorOrSuperAdmin();

        Worker worker = workerRepository.findWorkerByIdAndIsActiveTrue(id).orElseThrow(
                () -> new NotFoundException("Worker with id " + id + " not found")
        );
        workerMapper.updateFully(worker, workerPutRequest);
        worker = workerRepository.save(worker);
        return IamResponse.createSuccessful(workerMapper.toDTO(worker));
    }

    @Override
    public IamResponse<WorkerDTO> updatePartiallyWorker(Integer id, WorkerPatchRequest workerPatchRequest) {
        accessValidator.validateDirectorOrSuperAdmin();

        Worker worker = workerRepository.findWorkerByIdAndIsActiveTrue(id).orElseThrow(
                () -> new NotFoundException("Worker with id " + id + " not found")
        );
        workerMapper.updatePartially(worker, workerPatchRequest);
        worker = workerRepository.save(worker);
        return IamResponse.createSuccessful(workerMapper.toDTO(worker));
    }

    @Override
    public void deleteWorker(Integer id) {
        accessValidator.validateDirectorOrSuperAdmin();

        Worker worker = workerRepository.findWorkerByIdAndIsActiveTrue(id).orElseThrow(
                () -> new NotFoundException("Worker with id " + id + " not found")
        );
        worker.setActive(false);
        workerRepository.save(worker);
    }
}
