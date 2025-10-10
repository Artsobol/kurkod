package io.github.artsobol.kurkod.repository;

import io.github.artsobol.kurkod.model.entity.Chicken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ChickenRepository extends JpaRepository<Chicken, Integer> {

    Optional<Chicken> findChickenByIdAndDeletedFalse(int id);

    List<Chicken> findAllByDeletedFalse();
}
