package io.github.artsobol.kurkod.feature.passport.repository;

import io.github.artsobol.kurkod.feature.passport.entity.Passport;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PassportRepository extends JpaRepository<Passport, Long> {

  Optional<Passport> findPassportByWorkerIdAndIsActiveTrue(Long workerId);

  void deletePassportByWorkerId(Long workerId);
}
