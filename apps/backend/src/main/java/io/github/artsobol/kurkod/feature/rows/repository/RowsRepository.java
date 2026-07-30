package io.github.artsobol.kurkod.feature.rows.repository;

import io.github.artsobol.kurkod.feature.rows.entity.Rows;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RowsRepository extends JpaRepository<Rows, Long> {
    Optional<Rows> findByWorkshop_IdAndRowNumberAndIsActiveTrue(Long workshopId, Integer rowNumber);

    List<Rows> findAllByWorkshop_IdAndIsActiveTrue(Long workshopId);

    boolean existsByWorkshop_IdAndRowNumberAndIsActiveTrue(Long workshopId, Integer rowNumber);
}
