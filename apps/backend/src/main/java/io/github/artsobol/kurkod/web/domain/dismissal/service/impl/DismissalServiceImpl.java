package io.github.artsobol.kurkod.web.domain.dismissal.service.impl;

import io.github.artsobol.kurkod.common.constants.ApiLogMessage;
import io.github.artsobol.kurkod.common.exception.NotFoundException;
import io.github.artsobol.kurkod.common.logging.LogHelper;
import io.github.artsobol.kurkod.security.facade.SecurityContextFacade;
import io.github.artsobol.kurkod.web.domain.dismissal.error.DismissalError;
import io.github.artsobol.kurkod.web.domain.dismissal.mapper.DismissalMapper;
import io.github.artsobol.kurkod.web.domain.dismissal.model.dto.DismissalDTO;
import io.github.artsobol.kurkod.web.domain.dismissal.model.entity.Dismissal;
import io.github.artsobol.kurkod.web.domain.dismissal.model.request.DismissalPatchRequest;
import io.github.artsobol.kurkod.web.domain.dismissal.model.request.DismissalPostRequest;
import io.github.artsobol.kurkod.web.domain.dismissal.model.request.DismissalPutRequest;
import io.github.artsobol.kurkod.web.domain.dismissal.repository.DismissalRepository;
import io.github.artsobol.kurkod.web.domain.dismissal.service.api.DismissalService;
import io.github.artsobol.kurkod.web.domain.worker.error.WorkerError;
import io.github.artsobol.kurkod.web.domain.worker.model.entity.Worker;
import io.github.artsobol.kurkod.web.domain.worker.repository.WorkerRepository;
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
@PreAuthorize("hasAnyAuthority('DIRECTOR', 'SUPER_ADMIN')")
public class DismissalServiceImpl implements DismissalService {

    private final DismissalRepository dismissalRepository;
    private final DismissalMapper dismissalMapper;
    private final SecurityContextFacade securityContextFacade;
    private final WorkerRepository workerRepository;

    private String getCurrentUsername() {
        return securityContextFacade.getCurrentUsername();
    }

    private Integer getCurrentUserId() {
        return securityContextFacade.getCurrentUserId();
    }

    @Override
    public DismissalDTO getByWorkerAndDismissed(Integer workerId, Integer dismissedId) {
        log.debug(ApiLogMessage.GET_ENTITY.getValue(), getCurrentUsername(), LogHelper.getEntityName(Dismissal.class), workerId, dismissedId);
        return dismissalMapper.toDTO(getDismissalByWorkerAndDismissed(workerId, dismissedId));
    }

    @Override
    public List<DismissalDTO> getAllByWorker(Integer workerId) {
        log.debug(ApiLogMessage.GET_ALL_ENTITIES.getValue(), getCurrentUsername(), LogHelper.getEntityName(Dismissal.class));
        return dismissalRepository.findAllByWorker_Id(workerId)
                .stream()
                .map(dismissalMapper::toDTO)
                .toList();
    }

    @Override
    public List<DismissalDTO> getAllByDismissed(Integer dismissedId) {
        log.debug(ApiLogMessage.GET_ALL_ENTITIES.getValue(), getCurrentUsername(), LogHelper.getEntityName(Dismissal.class));
        return dismissalRepository.findAllByWhoDismiss_Id(dismissedId)
                .stream()
                .map(dismissalMapper::toDTO)
                .toList();
    }

    @Override
    @Transactional
    public DismissalDTO create(DismissalPostRequest dismissalPostRequest) {
        Dismissal dismissal = dismissalMapper.toEntity(dismissalPostRequest);
        Worker worker = getWorkerById(dismissalPostRequest.getWorkerId());
        Worker whoDismiss = getWorkerById(getCurrentUserId());
        dismissal.setWorker(worker);
        dismissal.setWhoDismiss(whoDismiss);
        dismissalRepository.save(dismissal);
        log.info(ApiLogMessage.CREATE_ENTITY.getValue(), getCurrentUsername(), LogHelper.getEntityName(dismissal), dismissal.getId());
        return dismissalMapper.toDTO(dismissal);
    }

    @Override
    @Transactional
    public DismissalDTO replace(Integer workerId, DismissalPutRequest dismissalPutRequest) {
        Dismissal dismissal = getDismissalByWorkerId(workerId);
        dismissalMapper.replace(dismissal, dismissalPutRequest);
        dismissal = dismissalRepository.save(dismissal);
        log.info(ApiLogMessage.REPLACE_ENTITY.getValue(), getCurrentUsername(), LogHelper.getEntityName(dismissal), dismissal.getId());
        return dismissalMapper.toDTO(dismissal);
    }

    @Override
    @Transactional
    public DismissalDTO update(Integer workerId, DismissalPatchRequest dismissalPatchRequest) {
        Dismissal dismissal = getDismissalByWorkerId(workerId);
        dismissalMapper.update(dismissal, dismissalPatchRequest);
        dismissal = dismissalRepository.save(dismissal);
        log.info(ApiLogMessage.UPDATE_ENTITY.getValue(), getCurrentUsername(), LogHelper.getEntityName(dismissal), dismissal.getId());
        return dismissalMapper.toDTO(dismissal);
    }

    protected Worker getWorkerById(Integer id) {
        return workerRepository.findById(id).orElseThrow(
                () -> new NotFoundException(WorkerError.NOT_FOUND_BY_ID.format(id))
        );
    }

    protected Dismissal getDismissalByWorkerId(Integer id) {
        return dismissalRepository.findDismissalByWorker_Id(id)
                .orElseThrow(() -> new NotFoundException(DismissalError.NOT_FOUND_BY_ID.format(id)));
    }

    protected Dismissal getDismissalByWorkerAndDismissed(Integer workerId, Integer dismissId) {
        return dismissalRepository.findDismissalByWorker_IdAndWhoDismiss_Id(workerId, dismissId)
                .orElseThrow(() -> new NotFoundException(DismissalError.NOT_FOUND_BY_ID.format(workerId, dismissId)));
    }


}
