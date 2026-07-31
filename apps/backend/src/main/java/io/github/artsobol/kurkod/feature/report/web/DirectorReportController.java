package io.github.artsobol.kurkod.feature.report.web;

import io.github.artsobol.kurkod.feature.report.dto.response.BreedEggDiffReportResponse;
import io.github.artsobol.kurkod.feature.report.dto.response.ChickenEggStatsResponse;
import io.github.artsobol.kurkod.feature.report.dto.response.ChickensByWorkshopAndBreedResponse;
import io.github.artsobol.kurkod.feature.report.dto.response.FarmMonthlyReportResponse;
import io.github.artsobol.kurkod.feature.report.dto.response.WorkerReportDailyEggsResponse;
import io.github.artsobol.kurkod.feature.report.dto.response.WorkshopBreedTopResponse;
import io.github.artsobol.kurkod.feature.report.service.BreedReportService;
import io.github.artsobol.kurkod.feature.report.service.ChickenReportService;
import io.github.artsobol.kurkod.feature.report.service.FarmReportService;
import io.github.artsobol.kurkod.feature.report.service.WorkerReportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/reports/director", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@Tag(name = "Director Reports", description = "Director-level analytical reports")
public class DirectorReportController {

    private final FarmReportService farmReportService;
    private final BreedReportService breedReportService;
    private final ChickenReportService chickenReportService;
    private final WorkerReportService workerReportService;

    @Operation(summary = "Factory monthly report")
    @GetMapping("/factory/monthly")
    public ResponseEntity<FarmMonthlyReportResponse> getFactoryMonthly(
            @RequestParam int year,
            @RequestParam int month
                                                                              ) {

        FarmMonthlyReportResponse response = farmReportService.getMonthlyReport(year, month);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Breed egg difference report")
    @GetMapping("/breeds/egg-diff")
    public ResponseEntity<List<BreedEggDiffReportResponse>> getBreedEggDiff() {

        List<BreedEggDiffReportResponse> response = breedReportService.getEggDiff();
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Chickens distribution by workshop and breed")
    @GetMapping("/chickens/by-workshop-and-breed")
    public ResponseEntity<List<ChickensByWorkshopAndBreedResponse>> getChickensByWorkshopAndBreed() {

        List<ChickensByWorkshopAndBreedResponse> response = chickenReportService.getChickensByWorkshopAndBreed();
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Top workshop for a specific breed")
    @GetMapping("/chickens/top-workshop-by-breed")
    public ResponseEntity<WorkshopBreedTopResponse> getTopWorkshopByBreed(
            @RequestParam Long breedId
                                                                                 ) {

        WorkshopBreedTopResponse response = chickenReportService.getTopWorkshopByBreed(breedId);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Egg statistics with filters")
    @GetMapping("/chickens/egg-stats")
    public ResponseEntity<List<ChickenEggStatsResponse>> getChickenEggStats(
            @RequestParam(required = false) Integer weight,
            @RequestParam(required = false) Long breedId,
            @RequestParam(required = false) LocalDate birthDate
                                                                                   ) {

        List<ChickenEggStatsResponse> response = chickenReportService.getEggStats(weight, breedId, birthDate);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Daily average eggs per worker for a month")
    @GetMapping("/workers/daily-avg-eggs")
    public ResponseEntity<List<WorkerReportDailyEggsResponse>> getWorkerDailyEggs(
            @RequestParam int year,
            @RequestParam int month
                                                                                         ) {

        List<WorkerReportDailyEggsResponse> response = workerReportService.getWorkerDailyEggs(year, month);
        return ResponseEntity.ok(response);
    }
}
