package io.github.artsobol.kurkod.feature.breed.repository;

import io.github.artsobol.kurkod.feature.breed.entity.Breed;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BreedRepository extends JpaRepository<Breed, Long> {

  Optional<Breed> findByIdAndIsActiveTrue(Long id);

  Page<Breed> findAllByIsActiveTrue(Pageable pageable);

  boolean existsByNameAndIsActiveTrue(String name);
}
