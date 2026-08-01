package io.github.artsobol.kurkod.feature.cage.web;

import io.github.artsobol.kurkod.feature.cage.dto.request.CageCreateRequest;
import io.github.artsobol.kurkod.feature.cage.dto.request.CageUpdateRequest;
import io.github.artsobol.kurkod.feature.cage.dto.response.CageResponse;
import io.github.artsobol.kurkod.feature.cage.service.CageService;
import io.github.artsobol.kurkod.infrastructure.utils.EtagUtils;
import io.github.artsobol.kurkod.infrastructure.utils.LocationUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Tag(name = "Cage", description = "Cage operations")
@RequiredArgsConstructor
@RequestMapping("/rows/{rowId}/cage")
public class CageController {

  private final CageService cageService;

  @GetMapping("/{cageNumber}")
  @Operation(summary = "Get cage by cage number")
  public ResponseEntity<CageResponse> get(
      @PathVariable Long rowId,
      @PathVariable Integer cageNumber) {
    CageResponse response = cageService.find(rowId, cageNumber);
    return ResponseEntity.ok().eTag(EtagUtils.toEtag(response.version())).body(response);
  }

  @GetMapping
  @Operation(summary = "Get all cages")
  public List<CageResponse> getAll(
      @PathVariable Long rowId) {
    return cageService.findAll(rowId);
  }

  @PostMapping
  @Operation(summary = "Create cage")
  public ResponseEntity<CageResponse> create(
      @PathVariable Long rowId,
      @RequestBody @Valid CageCreateRequest cageCreateRequest) {
    CageResponse response = cageService.create(rowId, cageCreateRequest);
    return ResponseEntity.created(LocationUtils.buildLocation(response.cageNumber()))
        .eTag(EtagUtils.toEtag(response.version()))
        .body(response);
  }
  @PatchMapping("/{cageNumber}")
  @Operation(summary = "Update cage by cage number")
  public ResponseEntity<CageResponse> update(
      @PathVariable Long rowId,
      @PathVariable Integer cageNumber,
      @RequestBody @Valid CageUpdateRequest cageUpdateRequest,
      @RequestHeader(value = HttpHeaders.IF_MATCH, required = false) String ifMatch) {

    long expected = EtagUtils.parseIfMatch(ifMatch);
    CageResponse response = cageService.update(rowId, cageNumber, cageUpdateRequest, expected);
    return ResponseEntity.ok()
        .eTag(EtagUtils.toEtag(response.version()))
        .body(response);
  }

  @DeleteMapping("/{cageNumber}")
  @Operation(summary = "Delete cage by cage number")
  public ResponseEntity<Void> delete(
      @PathVariable Long rowId,
      @PathVariable Integer cageNumber,
      @RequestHeader(value = HttpHeaders.IF_MATCH, required = false) String ifMatch) {

    long expected = EtagUtils.parseIfMatch(ifMatch);
    cageService.delete(rowId, cageNumber, expected);
    return ResponseEntity.noContent().build();
  }
}
