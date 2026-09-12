package io.github.artsobol.kurkod.feature.breed.web;

import io.github.artsobol.kurkod.feature.breed.dto.request.BreedCreateRequest;
import io.github.artsobol.kurkod.feature.breed.dto.request.BreedUpdateRequest;
import io.github.artsobol.kurkod.feature.breed.dto.response.BreedResponse;
import io.github.artsobol.kurkod.feature.breed.service.BreedService;
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
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequiredArgsConstructor
@Tag(name = "Breeds", description = "Breed operations")
@RequestMapping(value = "/breeds", produces = MediaType.APPLICATION_JSON_VALUE)
public class BreedController {

  private final BreedService breedService;

  @GetMapping("/{breedId}")
  @Operation(summary = "Get breed by ID")
  public ResponseEntity<BreedResponse> getById(@PathVariable @Positive Long breedId) {
    BreedResponse response = breedService.getById(breedId);
    return ResponseEntity.ok().eTag(EtagUtils.toEtag(response.version())).body(response);
  }

  @GetMapping
  @Operation(summary = "Get a page of breeds")
  public PageResponse<BreedResponse> getPage(
      @RequestParam(defaultValue = "0") @PositiveOrZero int page,
      @RequestParam(defaultValue = "10") @Positive @Max(100) int size) {
    Pageable pageable =
        PageRequest.of(page, size, Sort.by(Sort.Order.asc("name"), Sort.Order.asc("id")));

    Page<BreedResponse> response = breedService.getPage(pageable);
    return PageResponse.from(response);
  }

  @PostMapping
  @Operation(summary = "Create breed")
  public ResponseEntity<BreedResponse> create(@Valid @RequestBody BreedCreateRequest request) {
    BreedResponse response = breedService.create(request);
    return ResponseEntity.created(LocationUtils.buildLocation(response.id()))
        .eTag(EtagUtils.toEtag(response.version()))
        .body(response);
  }

  @PatchMapping("/{breedId}")
  @Operation(summary = "Partially update breed")
  public ResponseEntity<BreedResponse> updateById(
      @PathVariable @Positive Long breedId,
      @Valid @RequestBody BreedUpdateRequest request,
      @RequestHeader(value = HttpHeaders.IF_MATCH, required = false) String ifMatch) {
    long expected = EtagUtils.parseIfMatch(ifMatch);
    BreedResponse response = breedService.update(breedId, request, expected);
    return ResponseEntity.ok().eTag(EtagUtils.toEtag(response.version())).body(response);
  }

  @DeleteMapping("/{breedId}")
  @Operation(summary = "Delete breed")
  public ResponseEntity<Void> deleteById(
      @PathVariable @Positive Long breedId,
      @RequestHeader(value = HttpHeaders.IF_MATCH, required = false) String ifMatch) {
    long expected = EtagUtils.parseIfMatch(ifMatch);
    breedService.delete(breedId, expected);
    return ResponseEntity.noContent().build();
  }
}
