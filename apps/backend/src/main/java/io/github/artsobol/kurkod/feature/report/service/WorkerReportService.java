package io.github.artsobol.kurkod.feature.report.service;

import io.github.artsobol.kurkod.feature.report.dto.response.WorkerReportDailyEggsResponse;

import java.util.List;

public interface WorkerReportService {

    List<WorkerReportDailyEggsResponse> getWorkerDailyEggs(int year, int month);
}
