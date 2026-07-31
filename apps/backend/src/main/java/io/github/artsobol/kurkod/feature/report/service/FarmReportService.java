package io.github.artsobol.kurkod.feature.report.service;

import io.github.artsobol.kurkod.feature.report.dto.response.FarmMonthlyReportResponse;

public interface FarmReportService {

    FarmMonthlyReportResponse getMonthlyReport(int year, int month);
}
