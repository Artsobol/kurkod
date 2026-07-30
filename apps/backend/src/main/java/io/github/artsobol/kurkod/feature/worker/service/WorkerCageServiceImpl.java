package io.github.artsobol.kurkod.feature.worker.service;

import io.github.artsobol.kurkod.exception.http.DataExistException;
import io.github.artsobol.kurkod.feature.cage.error.CageError;
import io.github.artsobol.kurkod.feature.cage.mapper.CageMapper;
import io.github.artsobol.kurkod.feature.cage.dto.response.CageDTO;
import io.github.artsobol.kurkod.feature.cage.entity.Cage;
import io.github.artsobol.kurkod.feature.cage.repository.CageRepository;
import io.github.artsobol.kurkod.feature.worker.error.WorkerError;
import io.github.artsobol.kurkod.feature.worker.mapper.WorkerMapper;
import io.github.artsobol.kurkod.feature.worker.dto.response.WorkerDTO;
import io.github.artsobol.kurkod.feature.worker.entity.Worker;
import io.github.artsobol.kurkod.feature.worker.entity.WorkerCage;
import io.github.artsobol.kurkod.feature.worker.repository.WorkerCageRepository;
import io.github.artsobol.kurkod.feature.worker.repository.WorkerRepository;
import io.github.artsobol.kurkod.feature.worker.service.WorkerCageService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@PreAuthorize("hasAnyAuthority('DIRECTOR', 'SUPER_ADMIN')")
@RequiredArgsConstructor
public class WorkerCageServiceImpl implements WorkerCageService {

    private final WorkerRepository workerRepository;
    private final CageRepository cageRepository;
    private final WorkerCageRepository workerCageRepository;
    private final CageMapper cageMapper;
    private final WorkerMapper workerMapper;

    @Override
    @Transactional(readOnly = true)
    public List<CageDTO> getWorkerCages(Long workerId) {
        return workerCageRepository.findAllByWorkerId(workerId)
                                   .stream()
                                   .map(WorkerCage::getCage)
                                   .map(cageMapper::toDto)
                                   .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<WorkerDTO> getCageWorkers(Long cageId) {

        return workerCageRepository.findAllByCageId(cageId)
                                   .stream()
                                   .map(WorkerCage::getWorker)
                                   .map(workerMapper::toDto)
                                   .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void assignCageToWorker(Long workerId, Long cageId) {
        if (workerCageRepository.existsByWorkerIdAndCageId(workerId, cageId)) {
            return;
        }

        Worker worker = workerRepository.findById(workerId).orElseThrow(() -> new DataExistException(WorkerError.NOT_FOUND_BY_ID, workerId));

        Cage cage = cageRepository.findById(cageId)
                                  .orElseThrow(() -> new DataExistException(CageError.NOT_FOUND_BY_ID, cageId) {
                                  });

        WorkerCage workerCage = new WorkerCage();
        workerCage.setWorker(worker);
        workerCage.setCage(cage);

        workerCageRepository.save(workerCage);
    }

    @Override
    @Transactional
    public void unassignCageFromWorker(Long workerId, Long cageId) {
        workerCageRepository.deleteByWorkerIdAndCageId(workerId, cageId);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean hasWorkerAnyCages(Long workerId) {
        return workerCageRepository.existsByWorkerId(workerId);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isCageServedByAnyWorker(Long cageId) {
        return workerCageRepository.existsByCageId(cageId);
    }
}
