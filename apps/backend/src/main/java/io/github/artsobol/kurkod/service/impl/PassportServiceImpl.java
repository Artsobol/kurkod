package io.github.artsobol.kurkod.service.impl;


import io.github.artsobol.kurkod.mapper.PassportMapper;
import io.github.artsobol.kurkod.model.dto.passport.PassportDTO;
import io.github.artsobol.kurkod.model.entity.Passport;
import io.github.artsobol.kurkod.model.entity.Worker;
import io.github.artsobol.kurkod.model.exception.NotFoundException;
import io.github.artsobol.kurkod.model.request.passport.PassportPatchRequest;
import io.github.artsobol.kurkod.model.request.passport.PassportPostRequest;
import io.github.artsobol.kurkod.model.request.passport.PassportPutRequest;
import io.github.artsobol.kurkod.model.response.IamResponse;
import io.github.artsobol.kurkod.repository.PassportRepository;
import io.github.artsobol.kurkod.repository.WorkerRepository;
import io.github.artsobol.kurkod.service.PassportService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PassportServiceImpl implements PassportService {

    private final PassportRepository passportRepository;
    private final PassportMapper passportMapper;
    private final WorkerRepository workerRepository;

    @Override
    public IamResponse<PassportDTO> getPassport(Integer workerId) {
        Passport passport = passportRepository.findPassportByWorkerIdAndIsActiveTrue(workerId).orElseThrow(
                () -> new NotFoundException("Chicken with id " + workerId + " not found")
        );
        return IamResponse.createSuccessful(passportMapper.toDto(passport));
    }

    @Override
    public IamResponse<PassportDTO> createPassport(Integer workerId, PassportPostRequest passportPostRequest) {
        Worker worker = workerRepository.findWorkerByIdAndIsActiveTrue(workerId).orElseThrow(
                () -> new NotFoundException("Worker with id " + workerId + " not found")
        );

        passportRepository.findPassportByWorkerIdAndIsActiveTrue(workerId)
                .ifPresent(p -> { throw new IllegalStateException("Worker already has an active passport"); });

        Passport passport = passportMapper.toEntity(passportPostRequest);
        passport.setWorker(worker);
        passport.setActive(true);
        passport = passportRepository.save(passport);
        return IamResponse.createSuccessful(passportMapper.toDto(passport));
    }

    @Override
    public IamResponse<PassportDTO> updateFullyPassport(Integer workerId, PassportPutRequest passportPutRequest) {
        Passport passport = passportRepository.findPassportByWorkerIdAndIsActiveTrue(workerId).orElseThrow(
                () -> new NotFoundException("Chicken with id " + workerId + " not found")
        );

        passportMapper.updateFully(passport, passportPutRequest);
        passport = passportRepository.save(passport);
        return IamResponse.createSuccessful(passportMapper.toDto(passport));
    }

    @Override
    public IamResponse<PassportDTO> updatePartiallyPassport(Integer workerId, PassportPatchRequest passportPatchRequest) {
        Passport passport = passportRepository.findPassportByWorkerIdAndIsActiveTrue(workerId).orElseThrow(
                () -> new NotFoundException("Chicken with id " + workerId + " not found")
        );

        passportMapper.updatePartially(passport, passportPatchRequest);
        passport = passportRepository.save(passport);
        return IamResponse.createSuccessful(passportMapper.toDto(passport));
    }

    @Override
    public void deletePassport(Integer workerId) {
        Passport passport = passportRepository.findPassportByWorkerIdAndIsActiveTrue(workerId).orElseThrow(
                () -> new NotFoundException("Passport with id " + workerId + " not found")
        );
        passport.setActive(false);
        passportRepository.save(passport);
    }
}
