package io.github.artsobol.kurkod.web.domain.cage.repository;

import io.github.artsobol.kurkod.web.domain.cage.model.entity.Cage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CageRepository extends JpaRepository<Cage, Integer> {
    Optional<Cage> findByRow_IdAndCageNumber(Integer rowId, Integer cageNumber);

    List<Cage> findAllByRow_IdOrderByCageNumberAsc(Integer rowId);

    boolean existsByRow_IdAndCageNumber(Integer rowId, Integer cageNumber);
}
