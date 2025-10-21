package io.github.artsobol.kurkod.web.domain.worker.service.impl;

import io.github.artsobol.kurkod.common.constants.ApiLogMessage;
import io.github.artsobol.kurkod.common.logging.LogHelper;
import io.github.artsobol.kurkod.security.facade.SecurityContextFacade;
import io.github.artsobol.kurkod.web.domain.worker.mapper.WorkerMapper;
import io.github.artsobol.kurkod.web.domain.worker.error.WorkerError;
import io.github.artsobol.kurkod.web.domain.worker.model.dto.WorkerDTO;
import io.github.artsobol.kurkod.web.domain.worker.model.entity.Worker;
import io.github.artsobol.kurkod.common.exception.NotFoundException;
import io.github.artsobol.kurkod.web.domain.worker.model.request.WorkerPatchRequest;
import io.github.artsobol.kurkod.web.domain.worker.model.request.WorkerPostRequest;
import io.github.artsobol.kurkod.web.domain.worker.model.request.WorkerPutRequest;
import io.github.artsobol.kurkod.web.domain.worker.repository.WorkerRepository;
import io.github.artsobol.kurkod.web.domain.worker.service.api.WorkerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
    public WorkerDTO get(Integer id) {
        log.debug(ApiLogMessage.GET_ENTITY.getValue(), getCurrentUsername(), LogHelper.getEntityName(Worker.class), id);
        return workerMapper.toDTO(getWorkerById(id));
    }

    @Override
    @PreAuthorize("hasAnyAuthority('DIRECTOR', 'SUPER_ADMIN')")
    public List<WorkerDTO> getAll() {
        log.debug(ApiLogMessage.GET_ALL_ENTITIES.getValue(), getCurrentUsername(), LogHelper.getEntityName(Worker.class));
        return workerRepository.findAllByIsActiveTrue().stream().map(workerMapper::toDTO).toList();
    }

    @Override
    @Transactional
    @PreAuthorize("hasAnyAuthority('DIRECTOR', 'SUPER_ADMIN')")
    public WorkerDTO create(WorkerPostRequest workerPostRequest) {
        Worker worker = workerMapper.toEntity(workerPostRequest);
        worker = workerRepository.save(worker);
        log.info(ApiLogMessage.CREATE_ENTITY.getValue(), getCurrentUsername(), LogHelper.getEntityName(worker), worker.getId());
        return workerMapper.toDTO(worker);
    }

    @Override
    @Transactional
    @PreAuthorize("hasAnyAuthority('DIRECTOR', 'SUPER_ADMIN')")
    public WorkerDTO replace(Integer id, WorkerPutRequest workerPutRequest) {
        Worker worker = getWorkerById(id);
        workerMapper.updateFully(worker, workerPutRequest);
        worker = workerRepository.save(worker);
        log.info(ApiLogMessage.REPLACE_ENTITY.getValue(), getCurrentUsername(), LogHelper.getEntityName(worker), id);
        return workerMapper.toDTO(worker);
    }

    @Override
    @Transactional
    @PreAuthorize("hasAnyAuthority('DIRECTOR', 'SUPER_ADMIN')")
    public WorkerDTO update(Integer id, WorkerPatchRequest workerPatchRequest) {
        Worker worker = getWorkerById(id);
        workerMapper.updatePartially(worker, workerPatchRequest);
        worker = workerRepository.save(worker);
        log.info(ApiLogMessage.UPDATE_ENTITY.getValue(), getCurrentUsername(), LogHelper.getEntityName(worker), id);
        return workerMapper.toDTO(worker);
    }

    @Override
    @Transactional
    @PreAuthorize("hasAnyAuthority('DIRECTOR', 'SUPER_ADMIN')")
    public void delete(Integer id) {
        Worker worker = getWorkerById(id);
        worker.setActive(false);
        workerRepository.save(worker);
        log.info(ApiLogMessage.DELETE_ENTITY.getValue(), getCurrentUsername(), LogHelper.getEntityName(Worker.class), id);
    }

    protected Worker getWorkerById(Integer id) {
        return workerRepository.findWorkerByIdAndIsActiveTrue(id).orElseThrow(() ->
                new NotFoundException(WorkerError.NOT_FOUND_BY_ID.format(id)));
    }
}
