package io.github.artsobol.kurkod.feature.worker.service;

import io.github.artsobol.kurkod.exception.http.NotFoundException;
import io.github.artsobol.kurkod.feature.cage.mapper.CageMapper;
import io.github.artsobol.kurkod.feature.cage.dto.response.CageResponse;
import io.github.artsobol.kurkod.feature.cage.entity.Cage;
import io.github.artsobol.kurkod.feature.cage.repository.CageRepository;
import io.github.artsobol.kurkod.feature.worker.mapper.WorkerMapper;
import io.github.artsobol.kurkod.feature.worker.dto.response.WorkerResponse;
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
  public List<CageResponse> getWorkerCages(Long workerId) {
    return workerCageRepository.findAllByWorkerId(workerId).stream()
        .map(WorkerCage::getCage)
        .map(cageMapper::toResponse)
        .collect(Collectors.toList());
  }

  @Override
  @Transactional(readOnly = true)
  public List<WorkerResponse> getCageWorkers(Long cageId) {

    return workerCageRepository.findAllByCageId(cageId).stream()
        .map(WorkerCage::getWorker)
        .map(workerMapper::toResponse)
        .collect(Collectors.toList());
  }

  @Override
  @Transactional
  public void assignCageToWorker(Long workerId, Long cageId) {
    if (workerCageRepository.existsByWorkerIdAndCageId(workerId, cageId)) {
      return;
    }

    Worker worker =
        workerRepository
            .findById(workerId)
            .orElseThrow(() -> new NotFoundException("worker.not.found", workerId));

    Cage cage =
        cageRepository
            .findById(cageId)
            .orElseThrow(() -> new NotFoundException("cage.not.found", cageId));

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
