package io.github.artsobol.kurkod.feature.diet.repository;

import io.github.artsobol.kurkod.feature.diet.entity.Diet;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DietRepository extends JpaRepository<Diet, Long> {

  Optional<Diet> findByIdAndIsActiveTrue(Long id);

  Page<Diet> findAllByIsActiveTrue(Pageable pageable);

  boolean existsByCodeAndIsActiveTrue(String code);
}
