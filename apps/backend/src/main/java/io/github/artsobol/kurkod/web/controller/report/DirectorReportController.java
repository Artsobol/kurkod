package io.github.artsobol.kurkod.web.controller.report;

import io.github.artsobol.kurkod.web.domain.report.farm.dto.FarmMonthlyReportDTO;
import io.github.artsobol.kurkod.web.domain.report.service.api.FarmReportService;
import io.github.artsobol.kurkod.web.domain.report.breed.model.dto.BreedEggDiffReportDTO;
import io.github.artsobol.kurkod.web.domain.report.breed.serivce.api.BreedReportService;
import io.github.artsobol.kurkod.web.domain.report.chicken.model.dto.ChickenEggStatsDTO;
import io.github.artsobol.kurkod.web.domain.report.chicken.model.dto.ChickensByWorkshopAndBreedDTO;
import io.github.artsobol.kurkod.web.domain.report.chicken.model.dto.WorkshopBreedTopDTO;
import io.github.artsobol.kurkod.web.domain.report.chicken.service.api.ChickenReportService;
import io.github.artsobol.kurkod.web.domain.report.worker.model.dto.WorkerReportDailyEggsDTO;
import io.github.artsobol.kurkod.web.domain.report.worker.service.api.WorkerReportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/reports/director", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@Tag(name = "Director Reports", description = "Director-level analytical reports")
public class DirectorReportController {

    private final FarmReportService farmReportService;
    private final BreedReportService breedReportService;
    private final ChickenReportService chickenReportService;
    private final WorkerReportService workerReportService;

    @Operation(summary = "Factory monthly report")
    @GetMapping("/factory/monthly")
    public ResponseEntity<FarmMonthlyReportDTO> getFactoryMonthly(
            @RequestParam int year,
            @RequestParam int month
                                                                              ) {

        FarmMonthlyReportDTO response = farmReportService.getMonthlyReport(year, month);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Breed egg difference report")
    @GetMapping("/breeds/egg-diff")
    public ResponseEntity<List<BreedEggDiffReportDTO>> getBreedEggDiff() {

        List<BreedEggDiffReportDTO> response = breedReportService.getEggDiff();
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Chickens distribution by workshop and breed")
    @GetMapping("/chickens/by-workshop-and-breed")
    public ResponseEntity<List<ChickensByWorkshopAndBreedDTO>> getChickensByWorkshopAndBreed() {

        List<ChickensByWorkshopAndBreedDTO> response = chickenReportService.getChickensByWorkshopAndBreed();
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Top workshop for a specific breed")
    @GetMapping("/chickens/top-workshop-by-breed")
    public ResponseEntity<WorkshopBreedTopDTO> getTopWorkshopByBreed(
            @RequestParam Long breedId
                                                                                 ) {

        WorkshopBreedTopDTO response = chickenReportService.getTopWorkshopByBreed(breedId);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Egg statistics with filters")
    @GetMapping("/chickens/egg-stats")
    public ResponseEntity<List<ChickenEggStatsDTO>> getChickenEggStats(
            @RequestParam(required = false) Integer weight,
            @RequestParam(required = false) Long breedId,
            @RequestParam(required = false) LocalDate birthDate
                                                                                   ) {

        List<ChickenEggStatsDTO> response = chickenReportService.getEggStats(weight, breedId, birthDate);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Daily average eggs per worker for a month")
    @GetMapping("/workers/daily-avg-eggs")
    public ResponseEntity<List<WorkerReportDailyEggsDTO>> getWorkerDailyEggs(
            @RequestParam int year,
            @RequestParam int month
                                                                                         ) {

        List<WorkerReportDailyEggsDTO> response = workerReportService.getWorkerDailyEggs(year, month);
        return ResponseEntity.ok(response);
    }
}
