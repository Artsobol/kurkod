package io.github.artsobol.kurkod.feature.chickenmovement.repository;

import io.github.artsobol.kurkod.feature.chickenmovement.entity.ChickenMovement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface ChickenMovementRepository extends JpaRepository<ChickenMovement, Long> {

  Optional<ChickenMovement> findTopByChicken_IdOrderByMovedAtDesc(Long chickenId);

  List<ChickenMovement> findAllByChicken_IdOrderByMovedAtDesc(Long chickenId);

  List<ChickenMovement> findAllByChicken_IdAndMovedAtBetweenOrderByMovedAtDesc(
      Long chickenId, Instant start, Instant end);

  List<ChickenMovement> findAllByChicken_IdAndFromCage_IdOrderByMovedAtDesc(
      Long chickenId, Long fromCageId);

  List<ChickenMovement> findAllByChicken_IdAndToCage_IdOrderByMovedAtDesc(
      Long chickenId, Long toCageId);
}
