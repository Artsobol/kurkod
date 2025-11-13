package io.github.artsobol.kurkod.web.domain.chicken.repository;

import io.github.artsobol.kurkod.web.domain.chicken.model.entity.Chicken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ChickenRepository extends JpaRepository<Chicken, Integer> {

    Optional<Chicken> findChickenById(int id);

    List<Chicken> findAll();
}
