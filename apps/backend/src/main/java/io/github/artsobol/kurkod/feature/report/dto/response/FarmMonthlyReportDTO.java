package io.github.artsobol.kurkod.feature.report.dto.response;

import io.github.artsobol.kurkod.feature.report.dto.response.BreedWorkshopMonthlyReportDTO;

import java.util.List;

public record FarmMonthlyReportDTO(
        int year,
        int month,
        List<BreedWorkshopMonthlyReportDTO> stats,
        long totalChickens,
        long totalEggs
) {}