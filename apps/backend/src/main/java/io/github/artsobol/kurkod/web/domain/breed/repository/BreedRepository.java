package io.github.artsobol.kurkod.web.domain.breed.repository;

import io.github.artsobol.kurkod.web.domain.breed.model.entity.Breed;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BreedRepository extends JpaRepository<Breed, Long> {

    Optional<Breed> findBreedByIdAndIsActiveTrue(Long id);

    List<Breed> findAllByIsActiveTrue();

    Page<Breed> findAllByIsActiveTrue(Pageable pageable);

    boolean existsByNameAndIsActiveTrue(String name);
}
