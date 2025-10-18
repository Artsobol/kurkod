package io.github.artsobol.kurkod.web.domain.worker.repository;

import io.github.artsobol.kurkod.web.domain.worker.model.entity.Worker;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface WorkerRepository extends JpaRepository<Worker, Integer> {

    Optional<Worker> findWorkerByIdAndIsActiveTrue(int id);

    List<Worker> findAllByIsActiveTrue();
}
