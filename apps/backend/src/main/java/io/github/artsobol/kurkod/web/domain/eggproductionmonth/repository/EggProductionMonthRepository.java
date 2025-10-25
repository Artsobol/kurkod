package io.github.artsobol.kurkod.web.domain.eggproductionmonth.repository;

import io.github.artsobol.kurkod.web.domain.eggproductionmonth.model.entity.EggProductionMonth;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EggProductionMonthRepository extends JpaRepository<EggProductionMonth, Integer> {

    List<EggProductionMonth> findAllByChicken_IdAndIsActiveTrue(int chickenId);

    List<EggProductionMonth> findAllByChicken_IdAndYearAndIsActiveTrue(int chickenId, int year);

    Optional<EggProductionMonth> findByChicken_IdAndMonthAndYearAndIsActiveTrue(int chickenId, int month, int year);

    boolean existsByChicken_IdAndMonthAndYearAndIsActiveTrue(int chickenId, int month, int year);
}
