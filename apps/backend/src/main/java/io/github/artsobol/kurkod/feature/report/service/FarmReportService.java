package io.github.artsobol.kurkod.feature.report.service;

import io.github.artsobol.kurkod.feature.report.dto.response.FarmMonthlyReportDTO;

public interface FarmReportService {

    FarmMonthlyReportDTO getMonthlyReport(int year, int month);
}
