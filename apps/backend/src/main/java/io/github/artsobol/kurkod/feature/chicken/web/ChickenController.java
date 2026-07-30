package io.github.artsobol.kurkod.feature.chicken.web;

import io.github.artsobol.kurkod.infrastructure.web.dto.PageResponse;
import io.github.artsobol.kurkod.infrastructure.util.EtagUtils;
import io.github.artsobol.kurkod.infrastructure.util.LocationUtils;
import io.github.artsobol.kurkod.feature.chicken.dto.response.ChickenDTO;
import io.github.artsobol.kurkod.feature.chicken.dto.request.ChickenPatchRequest;
import io.github.artsobol.kurkod.feature.chicken.dto.request.ChickenPutRequest;
import io.github.artsobol.kurkod.feature.chicken.dto.request.ChickenPostRequest;
import io.github.artsobol.kurkod.feature.chicken.service.ChickenService;
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

import java.util.List;

@Validated
@RestController
@RequestMapping(value = "/api/v1/chickens", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@Tag(name = "Chickens", description = "Chicken operations")
public class ChickenController {

    private final ChickenService chickenService;

    @Operation(summary = "Create chicken")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ChickenDTO> create(@Valid @RequestBody ChickenPostRequest request) {
        ChickenDTO response = chickenService.create(request);
        return ResponseEntity.created(LocationUtils.buildLocation(response.id()))
                             .eTag(EtagUtils.toEtag(response.version()))
                             .body(response);
    }

    @Operation(summary = "Get all chickens")
    @GetMapping("/all")
    public ResponseEntity<List<ChickenDTO>> getAll() {
        List<ChickenDTO> response = chickenService.getAll();
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get a page of chickens")
    @GetMapping
    public PageResponse<ChickenDTO> getPage(
            @RequestParam(defaultValue = "0") @PositiveOrZero int page,
            @RequestParam(defaultValue = "10") @Positive @Max(100) int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Order.asc("id")));
        Page<ChickenDTO> response = chickenService.getPage(pageable);
        return PageResponse.from(response);
    }

    @Operation(summary = "Get chicken by ID")
    @GetMapping("/{id}")
    public ResponseEntity<ChickenDTO> get(
            @PathVariable Long id) {
        ChickenDTO response = chickenService.get(id);
        return ResponseEntity.ok()
                             .eTag(EtagUtils.toEtag(response.version()))
                             .body(response);
    }

    @Operation(summary = "Replace chicken")
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ChickenDTO> replace(
            @PathVariable Long id,
            @Valid @RequestBody ChickenPutRequest request,
            @RequestHeader(HttpHeaders.IF_MATCH) String ifMatch) {
        long expected = EtagUtils.parseIfMatch(ifMatch);
        ChickenDTO response = chickenService.replace(id, request, expected);
        return ResponseEntity.ok()
                             .eTag(EtagUtils.toEtag(response.version()))
                             .body(response);
    }

    @Operation(summary = "Partially update chicken")
    @PatchMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ChickenDTO> update(
            @PathVariable Long id,
            @Valid @RequestBody ChickenPatchRequest request,
            @RequestHeader(HttpHeaders.IF_MATCH) String ifMatch) {
        long expected = EtagUtils.parseIfMatch(ifMatch);
        ChickenDTO response = chickenService.update(id, request, expected);
        return ResponseEntity.ok()
                             .eTag(EtagUtils.toEtag(response.version()))
                             .body(response);
    }

    @Operation(summary = "Delete chicken")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Long id,
            @RequestHeader(HttpHeaders.IF_MATCH) String ifMatch) {
        long expected = EtagUtils.parseIfMatch(ifMatch);
        chickenService.delete(id, expected);
        return ResponseEntity.noContent().build();
    }
}
