package io.github.artsobol.kurkod.web.domain.breed.repository;

import io.github.artsobol.kurkod.web.domain.breed.model.entity.Breed;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BreedRepository extends JpaRepository<Breed, Integer> {

    Optional<Breed> findBreedByIdAndDeletedFalse(int id);

    List<Breed> findAllByDeletedFalse();
}
