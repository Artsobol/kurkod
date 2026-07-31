package io.github.artsobol.kurkod.feature.diet.repository;

import io.github.artsobol.kurkod.feature.diet.entity.Diet;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DietRepository extends JpaRepository<Diet, Long> {

    Optional<Diet> findDietByIdAndIsActiveTrue(Long id);

    List<Diet> findAllByIsActiveTrue();

    boolean existsByCodeAndIsActiveTrue(String code);
}
