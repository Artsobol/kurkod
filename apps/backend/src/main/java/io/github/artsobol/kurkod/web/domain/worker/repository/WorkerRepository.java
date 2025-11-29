package io.github.artsobol.kurkod.web.domain.worker.repository;

import io.github.artsobol.kurkod.web.domain.worker.model.entity.Worker;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface WorkerRepository extends JpaRepository<Worker, Long> {

    Optional<Worker> findWorkerByIdAndIsActiveTrue(Long id);

    List<Worker> findAllByIsActiveTrue();

    Page<Worker> findAllByIsActiveTrue(Pageable pageable);
}
