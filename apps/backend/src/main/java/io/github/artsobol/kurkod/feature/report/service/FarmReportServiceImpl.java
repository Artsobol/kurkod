package io.github.artsobol.kurkod.feature.report.service;

import io.github.artsobol.kurkod.feature.chicken.repository.ChickenRepository;
import io.github.artsobol.kurkod.feature.eggproductionmonth.repository.EggProductionMonthRepository;
import io.github.artsobol.kurkod.feature.report.dto.response.BreedWorkshopMonthlyReportDTO;
import io.github.artsobol.kurkod.feature.report.repository.FarmMonthlyStatsRepository;
import io.github.artsobol.kurkod.feature.report.dto.response.FarmMonthlyReportDTO;
import io.github.artsobol.kurkod.feature.report.service.FarmReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@PreAuthorize("hasAnyAuthority('DIRECTOR', 'SUPER_ADMIN')")
public class FarmReportServiceImpl implements FarmReportService {

    private final FarmMonthlyStatsRepository farmMonthlyStatsRepository;
    private final ChickenRepository chickenRepository;
    private final EggProductionMonthRepository eggProductionMonthRepository;

    @Override
    public FarmMonthlyReportDTO getMonthlyReport(int year, int month) {
        List<BreedWorkshopMonthlyReportDTO> stats = farmMonthlyStatsRepository.findBreedWorkshopMonthlyStats(year,
                                                                                                             month);

        long totalChickens = chickenRepository.countActiveChickens();

        long totalEggs = eggProductionMonthRepository.countEggsByMonth(year, month);

        return new FarmMonthlyReportDTO(year, month, stats, totalChickens, totalEggs);
    }
}
