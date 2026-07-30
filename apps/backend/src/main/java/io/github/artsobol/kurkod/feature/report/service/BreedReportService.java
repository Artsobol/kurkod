package io.github.artsobol.kurkod.feature.report.service;

import io.github.artsobol.kurkod.feature.report.dto.response.BreedEggDiffReportDTO;

import java.util.List;

public interface BreedReportService {

    List<BreedEggDiffReportDTO> getEggDiff();
}
