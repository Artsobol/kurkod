package io.github.artsobol.kurkod.repository;

import io.github.artsobol.kurkod.model.entity.Chicken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChickenRepository extends JpaRepository<Chicken, Integer> {
}
