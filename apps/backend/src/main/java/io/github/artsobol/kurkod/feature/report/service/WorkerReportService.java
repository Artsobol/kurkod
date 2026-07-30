package io.github.artsobol.kurkod.feature.report.service;

import io.github.artsobol.kurkod.feature.report.dto.response.WorkerReportDailyEggsDTO;

import java.util.List;

public interface WorkerReportService {

    List<WorkerReportDailyEggsDTO> getWorkerDailyEggs(int year, int month);
}
