package io.github.artsobol.kurkod.web.domain.workshop.repository;

import io.github.artsobol.kurkod.web.domain.workshop.model.entity.Workshop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WorkshopRepository extends JpaRepository<Workshop, Integer> {

    Optional<Workshop> findWorkshopByIdAndIsActiveTrue(int id);

    List<Workshop> findAllByIsActiveTrue();

    boolean existsByIdAndIsActiveTrue(int id);
}
