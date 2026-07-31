package io.github.artsobol.kurkod.feature.breed.web;

import io.github.artsobol.kurkod.feature.breed.dto.request.BreedCreateRequest;
import io.github.artsobol.kurkod.infrastructure.web.dto.PageResponse;
import io.github.artsobol.kurkod.infrastructure.util.EtagUtils;
import io.github.artsobol.kurkod.infrastructure.util.LocationUtils;
import io.github.artsobol.kurkod.feature.breed.dto.response.BreedDTO;
import io.github.artsobol.kurkod.feature.breed.dto.request.BreedUpdateRequest;
import io.github.artsobol.kurkod.feature.breed.service.BreedService;
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
@RequestMapping(value = "/api/v1/breeds", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@Tag(name = "Breeds", description = "Breed operations")
public class BreedController {

  private final BreedService breedService;

  @Operation(summary = "Get breed by ID")
  @GetMapping("/{breedId}")
  public ResponseEntity<BreedDTO> getById(@PathVariable Long breedId) {
    BreedDTO response = breedService.get(breedId);
    return ResponseEntity.ok().eTag(EtagUtils.toEtag(response.version())).body(response);
  }

  @Operation(summary = "Get a page of breeds")
  @GetMapping
  public PageResponse<BreedDTO> getPage(
      @RequestParam(defaultValue = "0") @PositiveOrZero int page,
      @RequestParam(defaultValue = "10") @Positive @Max(100) int size) {
    Pageable pageable =
        PageRequest.of(page, size, Sort.by(Sort.Order.asc("name"), Sort.Order.asc("id")));

    Page<BreedDTO> response = breedService.getPage(pageable);
    return PageResponse.from(response);
  }

  @Operation(summary = "Create breed")
  @PostMapping
  public ResponseEntity<BreedDTO> createBreed(@Valid @RequestBody BreedCreateRequest request) {
    BreedDTO response = breedService.create(request);
    return ResponseEntity.created(LocationUtils.buildLocation(response.id()))
        .eTag(EtagUtils.toEtag(response.version()))
        .body(response);
  }

  @Operation(summary = "Partially update breed")
  @PatchMapping("/{breedId}")
  public ResponseEntity<BreedDTO> updateById(
      @PathVariable Long breedId,
      @Valid @RequestBody BreedUpdateRequest request,
      @RequestHeader(HttpHeaders.IF_MATCH) String ifMatch) {
    long expected = EtagUtils.parseIfMatch(ifMatch);
    BreedDTO response = breedService.update(breedId, request, expected);
    return ResponseEntity.ok().eTag(EtagUtils.toEtag(response.version())).body(response);
  }

  @Operation(summary = "Delete breed")
  @DeleteMapping("/{breedId}")
  public ResponseEntity<Void> deleteById(
      @PathVariable Long breedId, @RequestHeader(HttpHeaders.IF_MATCH) String ifMatch) {
    long expected = EtagUtils.parseIfMatch(ifMatch);
    breedService.delete(breedId, expected);
    return ResponseEntity.noContent().build();
  }
}
