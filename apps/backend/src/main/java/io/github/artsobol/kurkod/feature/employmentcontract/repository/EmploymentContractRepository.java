package io.github.artsobol.kurkod.feature.employmentcontract.repository;

import io.github.artsobol.kurkod.feature.employmentcontract.entity.EmploymentContract;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmploymentContractRepository extends JpaRepository<EmploymentContract, Long> {
  Optional<EmploymentContract> findEmploymentContractByWorkerIdAndIsActiveTrue(Long workerId);
}
