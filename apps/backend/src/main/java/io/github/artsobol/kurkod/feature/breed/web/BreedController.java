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
@RequestMapping(value = "/breeds", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@Tag(name = "Breeds", description = "Breed operations")
public class BreedController {

  private final BreedService breedService;

  @Operation(summary = "Get breed by ID")
  @GetMapping("/{breedId}")
  public ResponseEntity<BreedResponse> getById(@PathVariable Long breedId) {
    BreedResponse response = breedService.get(breedId);
    return ResponseEntity.ok().eTag(EtagUtils.toEtag(response.version())).body(response);
  }

  @Operation(summary = "Get a page of breeds")
  @GetMapping
  public PageResponse<BreedResponse> getPage(
      @RequestParam(defaultValue = "0") @PositiveOrZero int page,
      @RequestParam(defaultValue = "10") @Positive @Max(100) int size) {
    Pageable pageable =
        PageRequest.of(page, size, Sort.by(Sort.Order.asc("name"), Sort.Order.asc("id")));

    Page<BreedResponse> response = breedService.getPage(pageable);
    return PageResponse.from(response);
  }

  @Operation(summary = "Create breed")
  @PostMapping
  public ResponseEntity<BreedResponse> createBreed(@Valid @RequestBody BreedCreateRequest request) {
    BreedResponse response = breedService.create(request);
    return ResponseEntity.created(LocationUtils.buildLocation(response.id()))
        .eTag(EtagUtils.toEtag(response.version()))
        .body(response);
  }

  @Operation(summary = "Partially update breed")
  @PatchMapping("/{breedId}")
  public ResponseEntity<BreedResponse> updateById(
      @PathVariable Long breedId,
      @Valid @RequestBody BreedUpdateRequest request,
      @RequestHeader(value = HttpHeaders.IF_MATCH, required = false) String ifMatch) {
    long expected = EtagUtils.parseIfMatch(ifMatch);
    BreedResponse response = breedService.update(breedId, request, expected);
    return ResponseEntity.ok().eTag(EtagUtils.toEtag(response.version())).body(response);
  }

  @Operation(summary = "Delete breed")
  @DeleteMapping("/{breedId}")
  public ResponseEntity<Void> deleteById(
      @PathVariable Long breedId,
      @RequestHeader(value = HttpHeaders.IF_MATCH, required = false) String ifMatch) {
    long expected = EtagUtils.parseIfMatch(ifMatch);
    breedService.delete(breedId, expected);
    return ResponseEntity.noContent().build();
  }
}
