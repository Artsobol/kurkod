package io.github.artsobol.kurkod.feature.staff.web;

import io.github.artsobol.kurkod.feature.staff.dto.request.StaffCreateRequest;
import io.github.artsobol.kurkod.feature.staff.dto.request.StaffUpdateRequest;
import io.github.artsobol.kurkod.feature.staff.dto.response.StaffResponse;
import io.github.artsobol.kurkod.feature.staff.service.StaffService;
import io.github.artsobol.kurkod.infrastructure.util.EtagUtils;
import io.github.artsobol.kurkod.infrastructure.util.LocationUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/staff", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@Tag(name = "Staff", description = "Staff operations")
public class StaffController {

    private final StaffService staffService;

    @Operation(summary = "Get staff by ID")
    @GetMapping("/{id}")
    public ResponseEntity<StaffResponse> get(
            @PathVariable Long id) {

        StaffResponse response = staffService.get(id);
        return ResponseEntity.ok()
                             .eTag(EtagUtils.toEtag(response.version()))
                             .body(response);
    }

    @Operation(summary = "Get all staff")
    @GetMapping
    public ResponseEntity<List<StaffResponse>> getAll() {

        List<StaffResponse> response = staffService.getAll();
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Create staff position")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<StaffResponse> create(@Valid @RequestBody StaffCreateRequest staffCreateRequest) {

        StaffResponse response = staffService.create(staffCreateRequest);
        return ResponseEntity.created(LocationUtils.buildLocation(response.id()))
                             .eTag(EtagUtils.toEtag(response.version()))
                             .body(response);
    }
    @Operation(summary = "Partially update staff position")
    @PatchMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<StaffResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody StaffUpdateRequest staffUpdateRequest,
            @RequestHeader(HttpHeaders.IF_MATCH) String ifMatch) {

        long expected = EtagUtils.parseIfMatch(ifMatch);
        StaffResponse response = staffService.update(id, staffUpdateRequest, expected);
        return ResponseEntity.ok()
                             .eTag(EtagUtils.toEtag(response.version()))
                             .body(response);
    }

    @Operation(summary = "Delete staff position")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Long id,
            @RequestHeader(HttpHeaders.IF_MATCH) String ifMatch) {

        long expected = EtagUtils.parseIfMatch(ifMatch);
        staffService.delete(id, expected);
        return ResponseEntity.noContent().build();
    }
}
