package io.github.artsobol.kurkod.feature.report.dto.response;

import io.github.artsobol.kurkod.feature.report.dto.response.BreedWorkshopMonthlyReportResponse;

import java.util.List;

public record FarmMonthlyReportResponse(
    int year,
    int month,
    List<BreedWorkshopMonthlyReportResponse> stats,
    long totalChickens,
    long totalEggs) {}
