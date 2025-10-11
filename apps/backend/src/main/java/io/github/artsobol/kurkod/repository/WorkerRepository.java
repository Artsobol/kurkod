package io.github.artsobol.kurkod.repository;

import io.github.artsobol.kurkod.model.entity.Worker;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface WorkerRepository extends JpaRepository<Worker, Integer> {

    Optional<Worker> findWorkerByIdAndIsActiveTrue(int id);

    List<Worker> findAllByIsActiveTrue();
}
