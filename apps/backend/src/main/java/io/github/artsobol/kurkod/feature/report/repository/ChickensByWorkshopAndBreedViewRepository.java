package io.github.artsobol.kurkod.feature.report.repository;

import io.github.artsobol.kurkod.feature.report.entity.ChickensByWorkshopAndBreedView;

import io.github.artsobol.kurkod.feature.report.entity.ChickensByWorkshopAndBreedView;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChickensByWorkshopAndBreedViewRepository
    extends JpaRepository<ChickensByWorkshopAndBreedView, Long> {

  java.util.List<ChickensByWorkshopAndBreedView> findByBreedIdOrderByChickensCountDesc(
      Long breedId);
}
