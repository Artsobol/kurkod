package io.github.artsobol.kurkod.feature.workshop.web;

import io.github.artsobol.kurkod.infrastructure.util.EtagUtils;
import io.github.artsobol.kurkod.feature.workshop.dto.response.WorkshopDTO;
import io.github.artsobol.kurkod.feature.workshop.dto.request.WorkshopPatchRequest;
import io.github.artsobol.kurkod.feature.workshop.dto.request.WorkshopPostRequest;
import io.github.artsobol.kurkod.feature.workshop.dto.request.WorkshopPutRequest;
import io.github.artsobol.kurkod.feature.workshop.service.WorkshopService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static io.github.artsobol.kurkod.infrastructure.util.LocationUtils.buildLocation;

@RestController
@RequiredArgsConstructor
@Tag(name = "Workshops", description = "Workshops operations")
@RequestMapping(value = "/api/v1/workshops", produces = "application/json")
public class WorkshopController {

    private final WorkshopService workshopService;

    @GetMapping("/{id}")
    @Operation(summary = "Get workshop by ID")
    public ResponseEntity<WorkshopDTO> get(
            @PathVariable Long id) {

        WorkshopDTO response = workshopService.get(id);
        return ResponseEntity.ok()
                             .eTag(EtagUtils.toEtag(response.version()))
                             .body(response);
    }

    @GetMapping
    @Operation(summary = "Get all workshops")
    public ResponseEntity<List<WorkshopDTO>> getAll() {

        List<WorkshopDTO> response = workshopService.getAll();
        return ResponseEntity.ok(response);
    }

    @PostMapping
    @Operation(summary = "Create workshop")
    public ResponseEntity<WorkshopDTO> create(
            @RequestBody @Valid WorkshopPostRequest request) {

        WorkshopDTO response = workshopService.create(request);
        return ResponseEntity.created(buildLocation(response.id())).eTag(EtagUtils.toEtag(response.version())).body(
                response);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Replace workshop")
    public ResponseEntity<WorkshopDTO> replace(
            @PathVariable Long id,
            @RequestBody @Valid WorkshopPutRequest workshopPutRequest,
            @RequestHeader(HttpHeaders.IF_MATCH) String ifMatch) {

        long expected = EtagUtils.parseIfMatch(ifMatch);
        WorkshopDTO response = workshopService.replace(id, workshopPutRequest, expected);
        return ResponseEntity.ok()
                             .eTag(EtagUtils.toEtag(response.version()))
                             .body(response);
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Partially update workshop")
    public ResponseEntity<WorkshopDTO> update(
            @PathVariable Long id,
            @RequestBody @Valid WorkshopPatchRequest request,
            @RequestHeader(HttpHeaders.IF_MATCH) String ifMatch) {

        long expected = EtagUtils.parseIfMatch(ifMatch);
        WorkshopDTO response = workshopService.update(id, request, expected);
        return ResponseEntity.ok()
                             .eTag(EtagUtils.toEtag(response.version()))
                             .body(response);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete workshop")
    public ResponseEntity<Void> delete(
            @PathVariable Long id,
            @RequestHeader(HttpHeaders.IF_MATCH) String ifMatch) {

        long expected = EtagUtils.parseIfMatch(ifMatch);
        workshopService.delete(id, expected);
        return ResponseEntity.noContent().build();
    }
}
