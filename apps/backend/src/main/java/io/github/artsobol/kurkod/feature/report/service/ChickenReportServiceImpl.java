package io.github.artsobol.kurkod.feature.report.service;

import io.github.artsobol.kurkod.feature.report.repository.ChickenEggStatsViewRepository;
import io.github.artsobol.kurkod.feature.report.repository.ChickensByWorkshopAndBreedViewRepository;
import io.github.artsobol.kurkod.feature.report.dto.response.ChickenEggStatsResponse;
import io.github.artsobol.kurkod.feature.report.dto.response.ChickensByWorkshopAndBreedResponse;
import io.github.artsobol.kurkod.feature.report.dto.response.WorkshopBreedTopResponse;
import io.github.artsobol.kurkod.feature.report.service.ChickenReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@PreAuthorize("hasAnyAuthority('DIRECTOR', 'SUPER_ADMIN')")
@Transactional(readOnly = true)
public class ChickenReportServiceImpl implements ChickenReportService {

  private final ChickensByWorkshopAndBreedViewRepository viewRepository;
  private final ChickenEggStatsViewRepository eggStatsViewRepository;

  @Override
  public List<ChickensByWorkshopAndBreedResponse> getChickensByWorkshopAndBreed() {
    return viewRepository.findAll().stream()
        .map(
            v ->
                new ChickensByWorkshopAndBreedResponse(
                    v.getWorkshopId(),
                    v.getWorkshopNumber(),
                    v.getBreedId(),
                    v.getBreedName(),
                    v.getChickensCount()))
        .toList();
  }

  @Override
  public WorkshopBreedTopResponse getTopWorkshopByBreed(Long breedId) {
    return viewRepository.findByBreedIdOrderByChickensCountDesc(breedId).stream()
        .findFirst()
        .map(
            v ->
                new WorkshopBreedTopResponse(
                    v.getWorkshopId(),
                    v.getWorkshopNumber(),
                    v.getBreedId(),
                    v.getBreedName(),
                    v.getChickensCount()))
        .orElse(null);
  }

  @Override
  public List<ChickenEggStatsResponse> getEggStats(
      Integer weight, Long breedId, LocalDate birthDate) {
    return eggStatsViewRepository.findByFilters(weight, breedId, birthDate).stream()
        .map(
            v ->
                new ChickenEggStatsResponse(
                    v.getChickenId(),
                    v.getChickenName(),
                    v.getBreedId(),
                    v.getBreedName(),
                    v.getWeight(),
                    v.getBirthDate(),
                    v.getEggsCount()))
        .toList();
  }
}
