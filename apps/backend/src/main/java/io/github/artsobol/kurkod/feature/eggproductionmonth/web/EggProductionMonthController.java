package io.github.artsobol.kurkod.feature.eggproductionmonth.web;

import io.github.artsobol.kurkod.feature.eggproductionmonth.dto.request.EggProductionMonthCreateRequest;
import io.github.artsobol.kurkod.feature.eggproductionmonth.dto.request.EggProductionMonthUpdateRequest;
import io.github.artsobol.kurkod.feature.eggproductionmonth.dto.response.EggProductionMonthResponse;
import io.github.artsobol.kurkod.feature.eggproductionmonth.service.EggProductionMonthService;
import io.github.artsobol.kurkod.infrastructure.util.EtagUtils;
import io.github.artsobol.kurkod.infrastructure.util.LocationUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Tag(name = "Egg Production Month", description = "Egg Production Month operations")
@RequestMapping("/chickens/{chickenId}/egg-productions")
public class EggProductionMonthController {

    private final EggProductionMonthService eggProductionMonthService;

    @PostMapping("/{year}/{month}")
    @Operation(summary = "Create egg production month")
    public ResponseEntity<EggProductionMonthResponse> create(
            @PathVariable Long chickenId,
            @PathVariable Integer year,
            @PathVariable Integer month,
            @Valid @RequestBody EggProductionMonthCreateRequest request) {

        EggProductionMonthResponse response = eggProductionMonthService.create(chickenId, month, year, request);
        return ResponseEntity.created(LocationUtils.buildLocation()).eTag(EtagUtils.toEtag(response.version())).body(
                response);
    }

    @GetMapping("/{year}/{month}")
    @Operation(summary = "Get egg production month by chicken ID, year and month")
    public ResponseEntity<EggProductionMonthResponse> getById(
            @PathVariable Long chickenId,
            @PathVariable Integer year,
            @PathVariable Integer month) {

        EggProductionMonthResponse response = eggProductionMonthService.get(chickenId, month, year);
        return ResponseEntity.ok()
                             .eTag(EtagUtils.toEtag(response.version()))
                             .body(response);
    }

    @GetMapping
    @Operation(summary = "Get all egg production months")
    public ResponseEntity<List<EggProductionMonthResponse>> getAllByChickenId(
            @PathVariable Long chickenId) {

        List<EggProductionMonthResponse> response = eggProductionMonthService.getAllByChicken(chickenId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{year}")
    @Operation(summary = "Get all egg production months for year")
    public ResponseEntity<List<EggProductionMonthResponse>> getAllByChickenIdAndYear(
            @PathVariable Long chickenId,
            @PathVariable Integer year) {

        List<EggProductionMonthResponse> response = eggProductionMonthService.getAllByChickenAndYear(chickenId, year);
        return ResponseEntity.ok(response);
    }
    @PatchMapping("/{year}/{month}")
    @Operation(summary = "Update egg production month")
    public ResponseEntity<EggProductionMonthResponse> update(
            @PathVariable Long chickenId,
            @PathVariable Integer year,
            @PathVariable Integer month,
            @Valid @RequestBody EggProductionMonthUpdateRequest request,
            @RequestHeader(value = HttpHeaders.IF_MATCH, required = false) String ifMatch) {

        long expected = EtagUtils.parseIfMatch(ifMatch);
        EggProductionMonthResponse response = eggProductionMonthService.update(chickenId, month, year, request, expected);
        return ResponseEntity.ok()
                             .eTag(EtagUtils.toEtag(response.version()))
                             .body(response);
    }

    @DeleteMapping("/{year}/{month}")
    @Operation(summary = "Delete egg production month")
    public ResponseEntity<Void> delete(
            @PathVariable Long chickenId,
            @PathVariable Integer year,
            @PathVariable Integer month,
            @RequestHeader(value = HttpHeaders.IF_MATCH, required = false) String ifMatch) {

        long expected = EtagUtils.parseIfMatch(ifMatch);
        eggProductionMonthService.delete(chickenId, month, year, expected);
        return ResponseEntity.noContent().build();
    }

}
