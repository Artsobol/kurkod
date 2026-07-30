package io.github.artsobol.kurkod.feature.cage.web;

import io.github.artsobol.kurkod.infrastructure.util.EtagUtils;
import io.github.artsobol.kurkod.infrastructure.util.LocationUtils;
import io.github.artsobol.kurkod.feature.cage.dto.response.CageDTO;
import io.github.artsobol.kurkod.feature.cage.dto.request.CagePatchRequest;
import io.github.artsobol.kurkod.feature.cage.dto.request.CagePostRequest;
import io.github.artsobol.kurkod.feature.cage.dto.request.CagePutRequest;
import io.github.artsobol.kurkod.feature.cage.service.CageService;
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
@RequestMapping("/api/v1/rows/{rowId}/cage")
public class CageController {

  private final CageService cageService;

  @GetMapping("/{cageNumber}")
  @Operation(summary = "Get cage by cage number")
  public ResponseEntity<CageDTO> get(
      @PathVariable Long rowId,
      @PathVariable Integer cageNumber) {
    CageDTO response = cageService.find(rowId, cageNumber);
    return ResponseEntity.ok().eTag(EtagUtils.toEtag(response.version())).body(response);
  }

  @GetMapping
  @Operation(summary = "Get all cages")
  public List<CageDTO> getAll(
      @PathVariable Long rowId) {
    return cageService.findAll(rowId);
  }

  @PostMapping
  @Operation(summary = "Create cage")
  public ResponseEntity<CageDTO> create(
      @PathVariable Long rowId,
      @RequestBody @Valid CagePostRequest cagePostRequest) {
    CageDTO response = cageService.create(rowId, cagePostRequest);
    return ResponseEntity.created(LocationUtils.buildLocation(response.cageNumber()))
        .eTag(EtagUtils.toEtag(response.version()))
        .body(response);
  }

  @PutMapping("/{cageNumber}")
  @Operation(summary = "Replace cage by cage number")
  public ResponseEntity<CageDTO> replace(
      @PathVariable Long rowId,
      @PathVariable Integer cageNumber,
      @RequestBody @Valid CagePutRequest cagePutRequest,
      @RequestHeader(HttpHeaders.IF_MATCH) String ifMatch) {
    long expected = EtagUtils.parseIfMatch(ifMatch);
    CageDTO response = cageService.replace(rowId, cageNumber, cagePutRequest, expected);
    return ResponseEntity.ok()
        .eTag(EtagUtils.toEtag(response.version()))
        .body(response);
  }

  @PatchMapping("/{cageNumber}")
  @Operation(summary = "Update cage by cage number")
  public ResponseEntity<CageDTO> update(
      @PathVariable Long rowId,
      @PathVariable Integer cageNumber,
      @RequestBody @Valid CagePatchRequest cagePatchRequest,
      @RequestHeader(HttpHeaders.IF_MATCH) String ifMatch) {

    long expected = EtagUtils.parseIfMatch(ifMatch);
    CageDTO response = cageService.update(rowId, cageNumber, cagePatchRequest, expected);
    return ResponseEntity.ok()
        .eTag(EtagUtils.toEtag(response.version()))
        .body(response);
  }

  @DeleteMapping("/{cageNumber}")
  @Operation(summary = "Delete cage by cage number")
  public ResponseEntity<Void> delete(
      @PathVariable Long rowId,
      @PathVariable Integer cageNumber,
      @RequestHeader(HttpHeaders.IF_MATCH) String ifMatch) {

    long expected = EtagUtils.parseIfMatch(ifMatch);
    cageService.delete(rowId, cageNumber, expected);
    return ResponseEntity.noContent().build();
  }
}
