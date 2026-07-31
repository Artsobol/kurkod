package io.github.artsobol.kurkod.feature.report.service;

import io.github.artsobol.kurkod.feature.report.dto.response.ChickenEggStatsResponse;
import io.github.artsobol.kurkod.feature.report.dto.response.ChickensByWorkshopAndBreedResponse;
import io.github.artsobol.kurkod.feature.report.dto.response.WorkshopBreedTopResponse;

import java.time.LocalDate;
import java.util.List;

public interface ChickenReportService {

  List<ChickensByWorkshopAndBreedResponse> getChickensByWorkshopAndBreed();

  WorkshopBreedTopResponse getTopWorkshopByBreed(Long breedId);

  List<ChickenEggStatsResponse> getEggStats(Integer weight, Long breedId, LocalDate birthDate);
}
