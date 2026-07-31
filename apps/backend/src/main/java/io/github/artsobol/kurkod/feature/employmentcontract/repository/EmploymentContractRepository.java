package io.github.artsobol.kurkod.feature.employmentcontract.repository;

import io.github.artsobol.kurkod.feature.employmentcontract.entity.EmploymentContract;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmploymentContractRepository extends JpaRepository<EmploymentContract, Long> {
  Optional<EmploymentContract> findEmploymentContractByWorkerIdAndIsActiveTrue(Long workerId);
}
