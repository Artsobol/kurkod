package io.github.artsobol.kurkod.feature.passport.service;


import static io.github.artsobol.kurkod.infrastructure.utils.VersionUtils.checkVersion;

import io.github.artsobol.kurkod.exception.http.DataExistException;
import io.github.artsobol.kurkod.exception.http.NotFoundException;
import io.github.artsobol.kurkod.feature.passport.dto.request.PassportCreateRequest;
import io.github.artsobol.kurkod.feature.passport.dto.request.PassportUpdateRequest;
import io.github.artsobol.kurkod.feature.passport.dto.response.PassportResponse;
import io.github.artsobol.kurkod.feature.passport.entity.Passport;
import io.github.artsobol.kurkod.feature.passport.mapper.PassportMapper;
import io.github.artsobol.kurkod.feature.passport.repository.PassportRepository;
import io.github.artsobol.kurkod.feature.worker.entity.Worker;
import io.github.artsobol.kurkod.feature.worker.repository.WorkerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PassportServiceImpl implements PassportService {

    private final PassportRepository passportRepository;
    private final PassportMapper passportMapper;
    private final WorkerRepository workerRepository;


    @Override
    @PreAuthorize("hasAnyAuthority('DIRECTOR', 'SUPER_ADMIN')")
    public PassportResponse get(Long workerId) {
        return passportMapper.toResponse(getPassportByWorkerId(workerId));
    }

    @Override
    @Transactional
    @PreAuthorize("hasAnyAuthority('DIRECTOR', 'SUPER_ADMIN')")
    public PassportResponse create(Long workerId, PassportCreateRequest passportCreateRequest) {
        Worker worker = workerRepository.findWorkerByIdAndIsActiveTrue(workerId).orElseThrow(
                () -> new NotFoundException("worker.not.found", workerId)
        );

        passportRepository.findPassportByWorkerIdAndIsActiveTrue(workerId)
                .ifPresent(p -> {
                    throw new DataExistException("passport.already.exists", workerId);
                });

        Passport passport = passportMapper.toEntity(passportCreateRequest);
        passport.setWorker(worker);
        passport = passportRepository.save(passport);
        return passportMapper.toResponse(passport);
    }
    @Override
    @Transactional
    @PreAuthorize("hasAnyAuthority('DIRECTOR', 'SUPER_ADMIN')")
    public PassportResponse update(Long workerId, PassportUpdateRequest request, Long version) {
        Passport passport = getPassportByWorkerId(workerId);
        checkVersion(passport.getVersion(), version);
        passportMapper.updatePartially(passport, request);
        passport = passportRepository.save(passport);
        return passportMapper.toResponse(passport);
    }

    @Override
    @Transactional
    @PreAuthorize("hasAnyAuthority('DIRECTOR', 'SUPER_ADMIN')")
    public void delete(Long workerId, Long version) {
        Passport passport = getPassportByWorkerId(workerId);
        checkVersion(passport.getVersion(), version);
        passport.deactivate();
        passportRepository.save(passport);
    }

    protected Passport getPassportByWorkerId(Long workerId) {
        return passportRepository.findPassportByWorkerIdAndIsActiveTrue(workerId).orElseThrow(
                () -> new NotFoundException("passport.not.found.by.worker", workerId)
        );
    }
}
