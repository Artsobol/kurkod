package io.github.artsobol.kurkod.feature.diet.web;

import io.github.artsobol.kurkod.infrastructure.util.EtagUtils;
import io.github.artsobol.kurkod.infrastructure.util.LocationUtils;
import io.github.artsobol.kurkod.feature.diet.dto.response.DietDTO;
import io.github.artsobol.kurkod.feature.diet.dto.request.DietPatchRequest;
import io.github.artsobol.kurkod.feature.diet.dto.request.DietPostRequest;
import io.github.artsobol.kurkod.feature.diet.dto.request.DietPutRequest;
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
@RequestMapping("/api/v1/diets")
public class DietController {

    private final DietService dietService;

    @GetMapping("/{id}")
    @Operation(summary = "Get diet by ID")
    public ResponseEntity<DietDTO> get(
            @PathVariable Long id) {

        DietDTO response = dietService.get(id);
        return ResponseEntity.ok()
                             .eTag(EtagUtils.toEtag(response.version()))
                             .body(response);
    }

    @GetMapping
    @Operation(summary = "Get all diets")
    public ResponseEntity<Iterable<DietDTO>> getAll() {

        Iterable<DietDTO> response = dietService.getAll();
        return ResponseEntity.ok(response);
    }

    @PostMapping
    @Operation(summary = "Create diet")
    public ResponseEntity<DietDTO> create(@RequestBody @Valid DietPostRequest request) {

        DietDTO response = dietService.create(request);
        return ResponseEntity.created(LocationUtils.buildLocation(response.id()))
                             .eTag(EtagUtils.toEtag(response.version()))
                             .body(response);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Replace diet")
    public ResponseEntity<DietDTO> replace(
            @PathVariable Long id,
            @RequestBody @Valid DietPutRequest request,
            @RequestHeader(HttpHeaders.IF_MATCH) String ifMatch) {

        long expected = EtagUtils.parseIfMatch(ifMatch);
        DietDTO response = dietService.replace(id, request, expected);
        return ResponseEntity.ok()
                             .eTag(EtagUtils.toEtag(response.version()))
                             .body(response);
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Partially update diet")
    public ResponseEntity<DietDTO> update(
            @PathVariable Long id,
            @RequestBody @Valid DietPatchRequest request,
            @RequestHeader(HttpHeaders.IF_MATCH) String ifMatch) {

        long expected = EtagUtils.parseIfMatch(ifMatch);
        DietDTO response = dietService.update(id, request, expected);
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
