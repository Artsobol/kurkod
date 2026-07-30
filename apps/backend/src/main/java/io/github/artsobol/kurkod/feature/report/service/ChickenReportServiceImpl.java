package io.github.artsobol.kurkod.feature.report.service;

import io.github.artsobol.kurkod.feature.report.repository.ChickenEggStatsViewRepository;
import io.github.artsobol.kurkod.feature.report.repository.ChickensByWorkshopAndBreedViewRepository;
import io.github.artsobol.kurkod.feature.report.dto.response.ChickenEggStatsDTO;
import io.github.artsobol.kurkod.feature.report.dto.response.ChickensByWorkshopAndBreedDTO;
import io.github.artsobol.kurkod.feature.report.dto.response.WorkshopBreedTopDTO;
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
    public List<ChickensByWorkshopAndBreedDTO> getChickensByWorkshopAndBreed() {
        return viewRepository.findAll()
                             .stream()
                             .map(v -> new ChickensByWorkshopAndBreedDTO(
                                     v.getWorkshopId(),
                                     v.getWorkshopNumber(),
                                     v.getBreedId(),
                                     v.getBreedName(),
                                     v.getChickensCount()
                             ))
                             .toList();
    }

    @Override
    public WorkshopBreedTopDTO getTopWorkshopByBreed(Long breedId) {
        return viewRepository.findByBreedIdOrderByChickensCountDesc(breedId)
                             .stream()
                             .findFirst()
                             .map(v -> new WorkshopBreedTopDTO(
                                     v.getWorkshopId(),
                                     v.getWorkshopNumber(),
                                     v.getBreedId(),
                                     v.getBreedName(),
                                     v.getChickensCount()
                             ))
                             .orElse(null);
    }

    @Override
    public List<ChickenEggStatsDTO> getEggStats(Integer weight, Long breedId, LocalDate birthDate) {
        return eggStatsViewRepository.findByFilters(weight, breedId, birthDate)
                                     .stream()
                                     .map(v -> new ChickenEggStatsDTO(
                                             v.getChickenId(),
                                             v.getChickenName(),
                                             v.getBreedId(),
                                             v.getBreedName(),
                                             v.getWeight(),
                                             v.getBirthDate(),
                                             v.getEggsCount()
                                     ))
                                     .toList();
    }
}
