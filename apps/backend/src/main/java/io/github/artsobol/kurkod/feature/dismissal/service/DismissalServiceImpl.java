package io.github.artsobol.kurkod.feature.dismissal.service;

import io.github.artsobol.kurkod.infrastructure.constants.ApiLogMessage;
import io.github.artsobol.kurkod.exception.http.NotFoundException;
import io.github.artsobol.kurkod.infrastructure.logging.LogHelper;
import io.github.artsobol.kurkod.infrastructure.security.facade.SecurityContextFacade;
import io.github.artsobol.kurkod.feature.dismissal.error.DismissalError;
import io.github.artsobol.kurkod.feature.dismissal.mapper.DismissalMapper;
import io.github.artsobol.kurkod.feature.dismissal.dto.response.DismissalDTO;
import io.github.artsobol.kurkod.feature.dismissal.entity.Dismissal;
import io.github.artsobol.kurkod.feature.dismissal.dto.request.DismissalUpdateRequest;
import io.github.artsobol.kurkod.feature.dismissal.dto.request.DismissalCreateRequest;
import io.github.artsobol.kurkod.feature.dismissal.repository.DismissalRepository;
import io.github.artsobol.kurkod.feature.dismissal.service.DismissalService;
import io.github.artsobol.kurkod.feature.worker.error.WorkerError;
import io.github.artsobol.kurkod.feature.worker.entity.Worker;
import io.github.artsobol.kurkod.feature.worker.repository.WorkerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static io.github.artsobol.kurkod.infrastructure.util.VersionUtils.checkVersion;

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

    private Long getCurrentUserId() {
        return securityContextFacade.getCurrentUserId();
    }

    @Override
    public DismissalDTO getByWorkerAndDismissed(Long workerId, Long dismissedId) {
        log.debug(ApiLogMessage.GET_ENTITY.getValue(), getCurrentUsername(), LogHelper.getEntityName(Dismissal.class), workerId, dismissedId);
        return dismissalMapper.toDTO(getDismissalByWorkerAndDismissed(workerId, dismissedId));
    }

    @Override
    public List<DismissalDTO> getAllByWorker(Long workerId) {
        log.debug(ApiLogMessage.GET_ALL_ENTITIES.getValue(), getCurrentUsername(), LogHelper.getEntityName(Dismissal.class));
        return dismissalRepository.findAllByWorker_Id(workerId)
                .stream()
                .map(dismissalMapper::toDTO)
                .toList();
    }

    @Override
    public List<DismissalDTO> getAllByDismissed(Long dismissedId) {
        log.debug(ApiLogMessage.GET_ALL_ENTITIES.getValue(), getCurrentUsername(), LogHelper.getEntityName(Dismissal.class));
        return dismissalRepository.findAllByWhoDismiss_Id(dismissedId)
                .stream()
                .map(dismissalMapper::toDTO)
                .toList();
    }

    @Override
    @Transactional
    public DismissalDTO create(DismissalCreateRequest request) {
        Dismissal dismissal = dismissalMapper.toEntity(request);
        Worker worker = getWorkerById(request.getWorkerId());
        Worker whoDismiss = getWorkerById(getCurrentUserId());
        dismissal.setWorker(worker);
        dismissal.setWhoDismiss(whoDismiss);
        dismissalRepository.save(dismissal);
        log.info(ApiLogMessage.CREATE_ENTITY.getValue(), getCurrentUsername(), LogHelper.getEntityName(dismissal), dismissal.getId());
        return dismissalMapper.toDTO(dismissal);
    }
    @Override
    @Transactional
    public DismissalDTO update(Long workerId, DismissalUpdateRequest request, Long version) {
        Dismissal dismissal = getDismissalByWorkerId(workerId);
        checkVersion(dismissal.getVersion(), version);
        dismissalMapper.update(dismissal, request);
        dismissal = dismissalRepository.save(dismissal);
        log.info(ApiLogMessage.UPDATE_ENTITY.getValue(), getCurrentUsername(), LogHelper.getEntityName(dismissal), dismissal.getId());
        return dismissalMapper.toDTO(dismissal);
    }

    protected Worker getWorkerById(Long id) {
        return workerRepository.findById(id).orElseThrow(
                () -> new NotFoundException("worker.not.found", id)
        );
    }

    protected Dismissal getDismissalByWorkerId(Long id) {
        return dismissalRepository.findDismissalByWorker_Id(id)
                .orElseThrow(() -> new NotFoundException("dismissal.not.found.by.worker", id));
    }

    protected Dismissal getDismissalByWorkerAndDismissed(Long workerId, Long dismissId) {
        return dismissalRepository.findDismissalByWorker_IdAndWhoDismiss_Id(workerId, dismissId)
                .orElseThrow(() -> new NotFoundException("dismissal.not.found.by.worker.and.user", workerId, dismissId));
    }


}
