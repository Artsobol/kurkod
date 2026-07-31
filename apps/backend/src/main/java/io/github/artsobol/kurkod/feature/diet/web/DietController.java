package io.github.artsobol.kurkod.feature.diet.web;

import io.github.artsobol.kurkod.infrastructure.util.EtagUtils;
import io.github.artsobol.kurkod.infrastructure.util.LocationUtils;
import io.github.artsobol.kurkod.feature.diet.dto.response.DietResponse;
import io.github.artsobol.kurkod.feature.diet.dto.request.DietUpdateRequest;
import io.github.artsobol.kurkod.feature.diet.dto.request.DietCreateRequest;
import io.github.artsobol.kurkod.feature.diet.service.DietService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Tag(name = "Diet", description = "Diet API")
@RequiredArgsConstructor
@RequestMapping("/diets")
public class DietController {

    private final DietService dietService;

    @GetMapping("/{id}")
    @Operation(summary = "Get diet by ID")
    public ResponseEntity<DietResponse> get(
            @PathVariable Long id) {

        DietResponse response = dietService.get(id);
        return ResponseEntity.ok()
                             .eTag(EtagUtils.toEtag(response.version()))
                             .body(response);
    }

    @GetMapping
    @Operation(summary = "Get all diets")
    public ResponseEntity<Iterable<DietResponse>> getAll() {

        Iterable<DietResponse> response = dietService.getAll();
        return ResponseEntity.ok(response);
    }

    @PostMapping
    @Operation(summary = "Create diet")
    public ResponseEntity<DietResponse> create(@RequestBody @Valid DietCreateRequest request) {

        DietResponse response = dietService.create(request);
        return ResponseEntity.created(LocationUtils.buildLocation(response.id()))
                             .eTag(EtagUtils.toEtag(response.version()))
                             .body(response);
    }
    @PatchMapping("/{id}")
    @Operation(summary = "Partially update diet")
    public ResponseEntity<DietResponse> update(
            @PathVariable Long id,
            @RequestBody @Valid DietUpdateRequest request,
            @RequestHeader(HttpHeaders.IF_MATCH) String ifMatch) {

        long expected = EtagUtils.parseIfMatch(ifMatch);
        DietResponse response = dietService.update(id, request, expected);
        return ResponseEntity.ok()
                             .eTag(EtagUtils.toEtag(response.version()))
                             .body(response);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete diet")
    public ResponseEntity<Void> delete(
            @PathVariable Long id,
            @RequestHeader(HttpHeaders.IF_MATCH) String ifMatch) {

        long expected = EtagUtils.parseIfMatch(ifMatch);
        dietService.delete(id, expected);
        return ResponseEntity.noContent().build();
    }
}
