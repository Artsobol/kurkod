package io.github.artsobol.kurkod.service.impl;


import io.github.artsobol.kurkod.mapper.PassportMapper;
import io.github.artsobol.kurkod.error.impl.PassportError;
import io.github.artsobol.kurkod.error.impl.WorkerError;
import io.github.artsobol.kurkod.model.dto.passport.PassportDTO;
import io.github.artsobol.kurkod.model.entity.Passport;
import io.github.artsobol.kurkod.model.entity.Worker;
import io.github.artsobol.kurkod.model.exception.NotFoundException;
import io.github.artsobol.kurkod.model.request.passport.PassportPatchRequest;
import io.github.artsobol.kurkod.model.request.passport.PassportPostRequest;
import io.github.artsobol.kurkod.model.request.passport.PassportPutRequest;
import io.github.artsobol.kurkod.repository.PassportRepository;
import io.github.artsobol.kurkod.repository.WorkerRepository;
import io.github.artsobol.kurkod.service.PassportService;
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
    @Transactional
    @PreAuthorize("hasAnyAuthority('DIRECTOR', 'SUPER_ADMIN')")
    public PassportDTO get(Integer workerId) {
        Passport passport = getPassportByWorkerId(workerId);
        return passportMapper.toDto(passport);
    }

    @Override
    @Transactional
    @PreAuthorize("hasAnyAuthority('DIRECTOR', 'SUPER_ADMIN')")
    public PassportDTO create(Integer workerId, PassportPostRequest passportPostRequest) {
        Worker worker = workerRepository.findWorkerByIdAndIsActiveTrue(workerId).orElseThrow(
                () -> new NotFoundException(WorkerError.NOT_FOUND_BY_ID.format(workerId))
        );

        passportRepository.findPassportByWorkerIdAndIsActiveTrue(workerId)
                .ifPresent(p -> { throw new IllegalStateException("Worker already has an active passport"); });

        Passport passport = passportMapper.toEntity(passportPostRequest);
        passport.setWorker(worker);
        passport.setActive(true);
        passport = passportRepository.save(passport);
        return passportMapper.toDto(passport);
    }

    @Override
    @Transactional
    @PreAuthorize("hasAnyAuthority('DIRECTOR', 'SUPER_ADMIN')")
    public PassportDTO replace(Integer workerId, PassportPutRequest passportPutRequest) {
        Passport passport = getPassportByWorkerId(workerId);

        passportMapper.updateFully(passport, passportPutRequest);
        passport = passportRepository.save(passport);
        return passportMapper.toDto(passport);
    }

    @Override
    @Transactional
    @PreAuthorize("hasAnyAuthority('DIRECTOR', 'SUPER_ADMIN')")
    public PassportDTO update(Integer workerId, PassportPatchRequest passportPatchRequest) {
        Passport passport = getPassportByWorkerId(workerId);
        passportMapper.updatePartially(passport, passportPatchRequest);
        passport = passportRepository.save(passport);
        return passportMapper.toDto(passport);
    }

    @Override
    @Transactional
    @PreAuthorize("hasAnyAuthority('DIRECTOR', 'SUPER_ADMIN')")
    public void delete(Integer workerId) {
        Passport passport = getPassportByWorkerId(workerId);
        passport.setActive(false);
        passportRepository.save(passport);
    }

    protected Passport getPassportByWorkerId(Integer workerId) {
        return passportRepository.findPassportByWorkerIdAndIsActiveTrue(workerId).orElseThrow(
                () -> new NotFoundException(PassportError.NOT_FOUND_BY_ID.format(workerId))
        );
    }
}
