package io.github.artsobol.kurkod.feature.report.service;

import io.github.artsobol.kurkod.feature.report.dto.response.ChickenEggStatsDTO;
import io.github.artsobol.kurkod.feature.report.dto.response.ChickensByWorkshopAndBreedDTO;
import io.github.artsobol.kurkod.feature.report.dto.response.WorkshopBreedTopDTO;

import java.time.LocalDate;
import java.util.List;

public interface ChickenReportService {

    List<ChickensByWorkshopAndBreedDTO> getChickensByWorkshopAndBreed();

    WorkshopBreedTopDTO getTopWorkshopByBreed(Long breedId);

    List<ChickenEggStatsDTO> getEggStats(Integer weight, Long breedId, LocalDate birthDate);
}