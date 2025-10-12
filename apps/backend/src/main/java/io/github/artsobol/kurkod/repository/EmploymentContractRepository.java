package io.github.artsobol.kurkod.repository;

import io.github.artsobol.kurkod.model.entity.EmploymentContract;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmploymentContractRepository extends JpaRepository<EmploymentContract, Integer> {
    Optional<EmploymentContract> findEmploymentContractByWorkerIdAndIsActiveTrue(int workerId);
}
