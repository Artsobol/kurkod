package io.github.artsobol.kurkod.feature.worker.service;

import io.github.artsobol.kurkod.infrastructure.constants.ApiLogMessage;
import io.github.artsobol.kurkod.infrastructure.logging.LogHelper;
import io.github.artsobol.kurkod.infrastructure.security.facade.SecurityContextFacade;
import io.github.artsobol.kurkod.feature.worker.mapper.WorkerMapper;
import io.github.artsobol.kurkod.feature.worker.error.WorkerError;
import io.github.artsobol.kurkod.feature.worker.dto.response.WorkerDTO;
import io.github.artsobol.kurkod.feature.worker.entity.Worker;
import io.github.artsobol.kurkod.exception.http.NotFoundException;
import io.github.artsobol.kurkod.feature.worker.dto.request.WorkerUpdateRequest;
import io.github.artsobol.kurkod.feature.worker.dto.request.WorkerCreateRequest;
import io.github.artsobol.kurkod.feature.worker.repository.WorkerRepository;
import io.github.artsobol.kurkod.feature.worker.service.WorkerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static io.github.artsobol.kurkod.infrastructure.util.VersionUtils.checkVersion;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class WorkerServiceImpl implements WorkerService {

    private final WorkerRepository workerRepository;
    private final WorkerMapper workerMapper;
    private final SecurityContextFacade securityContextFacade;

    private String getCurrentUsername() {
        return securityContextFacade.getCurrentUsername();
    }

    @Override
    @PreAuthorize("hasAnyAuthority('DIRECTOR', 'SUPER_ADMIN')")
    public WorkerDTO get(Long id) {
        log.debug(ApiLogMessage.GET_ENTITY.getValue(), getCurrentUsername(), LogHelper.getEntityName(Worker.class), id);
        return workerMapper.toDto(getWorkerById(id));
    }

    @Override
    @PreAuthorize("hasAnyAuthority('DIRECTOR', 'SUPER_ADMIN')")
    public List<WorkerDTO> getAll() {
        log.debug(ApiLogMessage.GET_ALL_ENTITIES.getValue(), getCurrentUsername(), LogHelper.getEntityName(Worker.class));
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
        log.info(ApiLogMessage.CREATE_ENTITY.getValue(), getCurrentUsername(), LogHelper.getEntityName(worker), worker.getId());
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
        log.info(ApiLogMessage.UPDATE_ENTITY.getValue(), getCurrentUsername(), LogHelper.getEntityName(worker), id);
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
        log.info(ApiLogMessage.DELETE_ENTITY.getValue(), getCurrentUsername(), LogHelper.getEntityName(Worker.class), id);
    }

    protected Worker getWorkerById(Long id) {
        return workerRepository.findWorkerByIdAndIsActiveTrue(id).orElseThrow(() ->
                new NotFoundException("worker.not.found", id));
    }
}
