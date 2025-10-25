package io.github.artsobol.kurkod.web.domain.chickenmovement.repository;

import io.github.artsobol.kurkod.web.domain.chickenmovement.model.entity.ChickenMovement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ChickenMovementRepository extends JpaRepository<ChickenMovement, Integer> {

    Optional<ChickenMovement> findTopByChicken_IdOrderByMovedAtDesc(Integer chickenId);

    List<ChickenMovement> findAllByChicken_IdOrderByMovedAtDesc(Integer chickenId);

    List<ChickenMovement> findAllByChicken_IdAndMovedAtBetweenOrderByMovedAtDesc(
            Integer chickenId,
            LocalDateTime start,
            LocalDateTime end
    );

    List<ChickenMovement> findAllByChicken_IdAndFromCage_IdOrderByMovedAtDesc(Integer chickenId, Integer fromCageId);

    List<ChickenMovement> findAllByChicken_IdAndToCage_IdOrderByMovedAtDesc(Integer chickenId, Integer toCageId);
}
