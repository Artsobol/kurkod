package io.github.artsobol.kurkod.feature.worker.service;

import io.github.artsobol.kurkod.feature.worker.mapper.WorkerMapper;
import io.github.artsobol.kurkod.feature.worker.dto.response.WorkerDTO;
import io.github.artsobol.kurkod.feature.worker.entity.Worker;
import io.github.artsobol.kurkod.exception.http.NotFoundException;
import io.github.artsobol.kurkod.feature.worker.dto.request.WorkerUpdateRequest;
import io.github.artsobol.kurkod.feature.worker.dto.request.WorkerCreateRequest;
import io.github.artsobol.kurkod.feature.worker.repository.WorkerRepository;
import io.github.artsobol.kurkod.feature.worker.service.WorkerService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static io.github.artsobol.kurkod.infrastructure.util.VersionUtils.checkVersion;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class WorkerServiceImpl implements WorkerService {

    private final WorkerRepository workerRepository;
    private final WorkerMapper workerMapper;


    @Override
    @PreAuthorize("hasAnyAuthority('DIRECTOR', 'SUPER_ADMIN')")
    public WorkerDTO get(Long id) {
        return workerMapper.toDto(getWorkerById(id));
    }

    @Override
    @PreAuthorize("hasAnyAuthority('DIRECTOR', 'SUPER_ADMIN')")
    public List<WorkerDTO> getAll() {
        return workerRepository.findAllByIsActiveTrue().stream().map(workerMapper::toDto).toList();
    }

    @Override
    public Page<WorkerDTO> getPage(Pageable pageable) {
        return workerRepository.findAllByIsActiveTrue(pageable).map(workerMapper::toDto);
    }

    @Override
    @Transactional
    @PreAuthorize("hasAnyAuthority('DIRECTOR', 'SUPER_ADMIN')")
    public WorkerDTO create(WorkerCreateRequest request) {
        Worker worker = workerMapper.toEntity(request);
        worker = workerRepository.save(worker);
        return workerMapper.toDto(worker);
    }
    @Override
    @Transactional
    @PreAuthorize("hasAnyAuthority('DIRECTOR', 'SUPER_ADMIN')")
    public WorkerDTO update(Long id, WorkerUpdateRequest request, Long version) {
        Worker worker = getWorkerById(id);
        checkVersion(worker.getVersion(), version);
        workerMapper.updatePartially(worker, request);
        worker = workerRepository.save(worker);
        return workerMapper.toDto(worker);
    }

    @Override
    @Transactional
    @PreAuthorize("hasAnyAuthority('DIRECTOR', 'SUPER_ADMIN')")
    public void delete(Long id, Long version) {
        Worker worker = getWorkerById(id);
        checkVersion(worker.getVersion(), version);
        worker.setActive(false);
        workerRepository.save(worker);
    }

    protected Worker getWorkerById(Long id) {
        return workerRepository.findWorkerByIdAndIsActiveTrue(id).orElseThrow(() ->
                new NotFoundException("worker.not.found", id));
    }
}
