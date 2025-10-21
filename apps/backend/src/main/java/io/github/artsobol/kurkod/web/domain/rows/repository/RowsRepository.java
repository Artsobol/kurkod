package io.github.artsobol.kurkod.web.domain.rows.repository;

import io.github.artsobol.kurkod.web.domain.rows.model.entity.Rows;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RowsRepository extends JpaRepository<Rows, Integer> {
    Optional<Rows> findByWorkshop_IdAndRowNumberAndIsActiveTrue(Integer workshopId, Integer rowNumber);

    List<Rows> findAllByWorkshop_IdAndIsActiveTrue(Integer workshopId);

    boolean existsByWorkshop_IdAndRowNumberAndIsActiveTrue(Integer workshopId, Integer rowNumber);
}
