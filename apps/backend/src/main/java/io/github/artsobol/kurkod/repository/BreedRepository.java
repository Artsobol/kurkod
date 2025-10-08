package io.github.artsobol.kurkod.repository;

import io.github.artsobol.kurkod.model.entity.Breed;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BreedRepository extends JpaRepository<Breed, Integer> {
}
