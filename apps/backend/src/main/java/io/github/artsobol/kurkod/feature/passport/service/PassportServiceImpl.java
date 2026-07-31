package io.github.artsobol.kurkod.feature.passport.service;


import io.github.artsobol.kurkod.infrastructure.constants.ApiLogMessage;
import io.github.artsobol.kurkod.infrastructure.logging.LogHelper;
import io.github.artsobol.kurkod.infrastructure.security.facade.SecurityContextFacade;
import io.github.artsobol.kurkod.feature.passport.mapper.PassportMapper;
import io.github.artsobol.kurkod.feature.passport.error.PassportError;
import io.github.artsobol.kurkod.feature.passport.service.PassportService;
import io.github.artsobol.kurkod.feature.worker.error.WorkerError;
import io.github.artsobol.kurkod.feature.passport.dto.response.PassportDTO;
import io.github.artsobol.kurkod.feature.passport.entity.Passport;
import io.github.artsobol.kurkod.feature.worker.entity.Worker;
import io.github.artsobol.kurkod.exception.http.DataExistException;
import io.github.artsobol.kurkod.exception.http.NotFoundException;
import io.github.artsobol.kurkod.feature.passport.dto.request.PassportUpdateRequest;
import io.github.artsobol.kurkod.feature.passport.dto.request.PassportCreateRequest;
import io.github.artsobol.kurkod.feature.passport.repository.PassportRepository;
import io.github.artsobol.kurkod.feature.worker.repository.WorkerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static io.github.artsobol.kurkod.infrastructure.util.VersionUtils.checkVersion;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PassportServiceImpl implements PassportService {

    private final PassportRepository passportRepository;
    private final PassportMapper passportMapper;
    private final WorkerRepository workerRepository;
    private final SecurityContextFacade securityContextFacade;

    private String getCurrentUsername() {
        return securityContextFacade.getCurrentUsername();
    }

    @Override
    @PreAuthorize("hasAnyAuthority('DIRECTOR', 'SUPER_ADMIN')")
    public PassportDTO get(Long workerId) {
        log.debug(ApiLogMessage.GET_ENTITY.getValue(), getCurrentUsername(), LogHelper.getEntityName(Passport.class), workerId);
        return passportMapper.toDto(getPassportByWorkerId(workerId));
    }

    @Override
    @Transactional
    @PreAuthorize("hasAnyAuthority('DIRECTOR', 'SUPER_ADMIN')")
    public PassportDTO create(Long workerId, PassportCreateRequest passportCreateRequest) {
        Worker worker = workerRepository.findWorkerByIdAndIsActiveTrue(workerId).orElseThrow(
                () -> new NotFoundException("worker.not.found", workerId)
        );

        passportRepository.findPassportByWorkerIdAndIsActiveTrue(workerId)
                .ifPresent(p -> {
                    throw new DataExistException("passport.already.exists", workerId);
                });

        Passport passport = passportMapper.toEntity(passportCreateRequest);
        passport.setWorker(worker);
        passport.setActive(true);
        passport = passportRepository.save(passport);
        log.info(ApiLogMessage.CREATE_ENTITY.getValue(), getCurrentUsername(), LogHelper.getEntityName(passport), workerId);
        return passportMapper.toDto(passport);
    }
    @Override
    @Transactional
    @PreAuthorize("hasAnyAuthority('DIRECTOR', 'SUPER_ADMIN')")
    public PassportDTO update(Long workerId, PassportUpdateRequest request, Long version) {
        Passport passport = getPassportByWorkerId(workerId);
        checkVersion(passport.getVersion(), version);
        passportMapper.updatePartially(passport, request);
        passport = passportRepository.save(passport);
        log.info(ApiLogMessage.UPDATE_ENTITY.getValue(), getCurrentUsername(), LogHelper.getEntityName(passport), workerId);
        return passportMapper.toDto(passport);
    }

    @Override
    @Transactional
    @PreAuthorize("hasAnyAuthority('DIRECTOR', 'SUPER_ADMIN')")
    public void delete(Long workerId, Long version) {
        Passport passport = getPassportByWorkerId(workerId);
        checkVersion(passport.getVersion(), version);
        passport.setActive(false);
        log.info(ApiLogMessage.DELETE_ENTITY.getValue(), getCurrentUsername(), LogHelper.getEntityName(Passport.class), workerId);
        passportRepository.save(passport);
    }

    protected Passport getPassportByWorkerId(Long workerId) {
        return passportRepository.findPassportByWorkerIdAndIsActiveTrue(workerId).orElseThrow(
                () -> new NotFoundException("passport.not.found.by.worker", workerId)
        );
    }
}
