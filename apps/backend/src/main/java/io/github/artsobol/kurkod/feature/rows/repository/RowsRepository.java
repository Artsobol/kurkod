package io.github.artsobol.kurkod.feature.rows.repository;

import io.github.artsobol.kurkod.feature.rows.entity.Rows;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RowsRepository extends JpaRepository<Rows, Long> {
  Optional<Rows> findByWorkshop_IdAndRowNumberAndIsActiveTrue(Long workshopId, Integer rowNumber);

  List<Rows> findAllByWorkshop_IdAndIsActiveTrue(Long workshopId);

  boolean existsByWorkshop_IdAndRowNumberAndIsActiveTrue(Long workshopId, Integer rowNumber);
}
