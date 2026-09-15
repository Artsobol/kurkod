package io.github.artsobol.kurkod.feature.diet.web;

import io.github.artsobol.kurkod.feature.diet.dto.request.DietCreateRequest;
import io.github.artsobol.kurkod.feature.diet.dto.request.DietUpdateRequest;
import io.github.artsobol.kurkod.feature.diet.dto.response.DietResponse;
import io.github.artsobol.kurkod.feature.diet.service.DietService;
import io.github.artsobol.kurkod.infrastructure.utils.EtagUtils;
import io.github.artsobol.kurkod.infrastructure.utils.LocationUtils;
import io.github.artsobol.kurkod.infrastructure.web.dto.PageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@Tag(name = "Diet", description = "Diet API")
@RequiredArgsConstructor
@RequestMapping("/diets")
public class DietController {

  private final DietService dietService;

  @GetMapping("/{dietId}")
  @Operation(summary = "Get diet by ID")
  public ResponseEntity<DietResponse> getById(@PathVariable @PositiveOrZero Long dietId) {
    DietResponse response = dietService.getById(dietId);
    return ResponseEntity.ok().eTag(EtagUtils.toEtag(response.version())).body(response);
  }

  @GetMapping
  @Operation(summary = "Get all diets")
  public PageResponse<DietResponse> getPage(@RequestParam(defaultValue = "0") @PositiveOrZero int page, @RequestParam(defaultValue = "10") @Positive @Max(100) int size) {
    Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Order.asc("title"), Sort.Order.asc("id")));

    Page<DietResponse> response = dietService.getPage(pageable);
    return PageResponse.from(response);
  }

  @PostMapping
  @Operation(summary = "Create diet")
  public ResponseEntity<DietResponse> create(@RequestBody @Valid DietCreateRequest request) {

    DietResponse response = dietService.create(request);
    return ResponseEntity.created(LocationUtils.buildLocation(response.id())).eTag(EtagUtils.toEtag(response.version())).body(response);
  }

  @PatchMapping("/{dietId}")
  @Operation(summary = "Partially update diet")
  public ResponseEntity<DietResponse> update(@PathVariable @PositiveOrZero Long dietId, @RequestBody @Valid DietUpdateRequest request, @RequestHeader(value = HttpHeaders.IF_MATCH, required = false) String ifMatch) {

    long expected = EtagUtils.parseIfMatch(ifMatch);
    DietResponse response = dietService.update(dietId, request, expected);
    return ResponseEntity.ok().eTag(EtagUtils.toEtag(response.version())).body(response);
  }

  @DeleteMapping("/{dietId}")
  @Operation(summary = "Delete diet")
  public ResponseEntity<Void> delete(@PathVariable @PositiveOrZero Long dietId, @RequestHeader(value = HttpHeaders.IF_MATCH, required = false) String ifMatch) {

    long expected = EtagUtils.parseIfMatch(ifMatch);
    dietService.delete(dietId, expected);
    return ResponseEntity.noContent().build();
  }
}
