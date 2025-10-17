package io.github.artsobol.kurkod.service.impl;

import io.github.artsobol.kurkod.mapper.WorkerMapper;
import io.github.artsobol.kurkod.error.impl.WorkerError;
import io.github.artsobol.kurkod.model.dto.worker.WorkerDTO;
import io.github.artsobol.kurkod.model.entity.Worker;
import io.github.artsobol.kurkod.model.exception.NotFoundException;
import io.github.artsobol.kurkod.model.request.worker.WorkerPatchRequest;
import io.github.artsobol.kurkod.model.request.worker.WorkerPostRequest;
import io.github.artsobol.kurkod.model.request.worker.WorkerPutRequest;
import io.github.artsobol.kurkod.repository.WorkerRepository;
import io.github.artsobol.kurkod.service.WorkerService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class WorkerServiceImpl implements WorkerService {

    private final WorkerRepository workerRepository;
    private final WorkerMapper workerMapper;

    @Override
    @Transactional
    @PreAuthorize("hasAnyAuthority('DIRECTOR', 'SUPER_ADMIN')")
    public WorkerDTO get(Integer id) {
        return workerMapper.toDTO(getWorkerById(id));
    }

    @Override
    @Transactional
    @PreAuthorize("hasAnyAuthority('DIRECTOR', 'SUPER_ADMIN')")
    public List<WorkerDTO> getAll() {
        return workerRepository.findAllByIsActiveTrue().stream().map(workerMapper::toDTO).toList();
    }

    @Override
    @Transactional
    @PreAuthorize("hasAnyAuthority('DIRECTOR', 'SUPER_ADMIN')")
    public WorkerDTO create(WorkerPostRequest workerPostRequest) {
        Worker worker = workerMapper.toEntity(workerPostRequest);
        worker = workerRepository.save(worker);
        return workerMapper.toDTO(worker);
    }

    @Override
    @Transactional
    @PreAuthorize("hasAnyAuthority('DIRECTOR', 'SUPER_ADMIN')")
    public WorkerDTO replace(Integer id, WorkerPutRequest workerPutRequest) {
        Worker worker = getWorkerById(id);
        workerMapper.updateFully(worker, workerPutRequest);
        worker = workerRepository.save(worker);
        return workerMapper.toDTO(worker);
    }

    @Override
    @Transactional
    @PreAuthorize("hasAnyAuthority('DIRECTOR', 'SUPER_ADMIN')")
    public WorkerDTO update(Integer id, WorkerPatchRequest workerPatchRequest) {
        Worker worker = getWorkerById(id);
        workerMapper.updatePartially(worker, workerPatchRequest);
        worker = workerRepository.save(worker);
        return workerMapper.toDTO(worker);
    }

    @Override
    @Transactional
    @PreAuthorize("hasAnyAuthority('DIRECTOR', 'SUPER_ADMIN')")
    public void delete(Integer id) {
        Worker worker = getWorkerById(id);
        worker.setActive(false);
        workerRepository.save(worker);
    }

    protected Worker getWorkerById(Integer id) {
        return workerRepository.findWorkerByIdAndIsActiveTrue(id).orElseThrow(() ->
                new NotFoundException(WorkerError.NOT_FOUND_BY_ID.format(id)));
    }
}
