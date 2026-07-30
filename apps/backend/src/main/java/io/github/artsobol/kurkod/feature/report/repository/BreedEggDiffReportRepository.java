package io.github.artsobol.kurkod.feature.report.repository;

import io.github.artsobol.kurkod.feature.report.entity.BreedEggDiffReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BreedEggDiffReportRepository extends JpaRepository<BreedEggDiffReport, Long> {
}
