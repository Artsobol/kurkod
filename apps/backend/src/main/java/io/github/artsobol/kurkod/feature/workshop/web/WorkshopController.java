package io.github.artsobol.kurkod.feature.workshop.web;

import static io.github.artsobol.kurkod.infrastructure.utils.LocationUtils.buildLocation;

import io.github.artsobol.kurkod.feature.workshop.dto.request.WorkshopCreateRequest;
import io.github.artsobol.kurkod.feature.workshop.dto.request.WorkshopUpdateRequest;
import io.github.artsobol.kurkod.feature.workshop.dto.response.WorkshopResponse;
import io.github.artsobol.kurkod.feature.workshop.service.WorkshopService;
import io.github.artsobol.kurkod.infrastructure.utils.EtagUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Tag(name = "Workshops", description = "Workshops operations")
@RequestMapping(value = "/workshops", produces = "application/json")
public class WorkshopController {

    private final WorkshopService workshopService;

    @GetMapping("/{id}")
    @Operation(summary = "Get workshop by ID")
    public ResponseEntity<WorkshopResponse> get(
            @PathVariable Long id) {

        WorkshopResponse response = workshopService.get(id);
        return ResponseEntity.ok()
                             .eTag(EtagUtils.toEtag(response.version()))
                             .body(response);
    }

    @GetMapping
    @Operation(summary = "Get all workshops")
    public ResponseEntity<List<WorkshopResponse>> getAll() {

        List<WorkshopResponse> response = workshopService.getAll();
        return ResponseEntity.ok(response);
    }

    @PostMapping
    @Operation(summary = "Create workshop")
    public ResponseEntity<WorkshopResponse> create(
            @RequestBody @Valid WorkshopCreateRequest request) {

        WorkshopResponse response = workshopService.create(request);
        return ResponseEntity.created(buildLocation(response.id())).eTag(EtagUtils.toEtag(response.version())).body(
                response);
    }
    @PatchMapping("/{id}")
    @Operation(summary = "Partially update workshop")
    public ResponseEntity<WorkshopResponse> update(
            @PathVariable Long id,
            @RequestBody @Valid WorkshopUpdateRequest request,
            @RequestHeader(value = HttpHeaders.IF_MATCH, required = false) String ifMatch) {

        long expected = EtagUtils.parseIfMatch(ifMatch);
        WorkshopResponse response = workshopService.update(id, request, expected);
        return ResponseEntity.ok()
                             .eTag(EtagUtils.toEtag(response.version()))
                             .body(response);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete workshop")
    public ResponseEntity<Void> delete(
            @PathVariable Long id,
            @RequestHeader(value = HttpHeaders.IF_MATCH, required = false) String ifMatch) {

        long expected = EtagUtils.parseIfMatch(ifMatch);
        workshopService.delete(id, expected);
        return ResponseEntity.noContent().build();
    }
}
