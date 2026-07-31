package io.github.artsobol.kurkod.feature.workshop.repository;

import io.github.artsobol.kurkod.feature.workshop.entity.Workshop;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkshopRepository extends JpaRepository<Workshop, Long> {

  Optional<Workshop> findWorkshopByIdAndIsActiveTrue(Long id);

  Optional<Workshop> findWorkshopByWorkshopNumberAndIsActiveTrue(Integer number);

  List<Workshop> findAllByIsActiveTrue();

  Page<Workshop> findAllByIsActiveTrue(Pageable pageable);

  boolean existsByWorkshopNumberAndIsActiveTrue(Integer number);
}
