package io.github.artsobol.kurkod.feature.staff.web;

import io.github.artsobol.kurkod.infrastructure.util.EtagUtils;
import io.github.artsobol.kurkod.infrastructure.util.LocationUtils;
import io.github.artsobol.kurkod.feature.staff.dto.response.StaffDTO;
import io.github.artsobol.kurkod.feature.staff.dto.request.StaffPatchRequest;
import io.github.artsobol.kurkod.feature.staff.dto.request.StaffPostRequest;
import io.github.artsobol.kurkod.feature.staff.dto.request.StaffPutRequest;
import io.github.artsobol.kurkod.feature.staff.service.StaffService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/staff", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@Tag(name = "Staff", description = "Staff operations")
public class StaffController {

    private final StaffService staffService;

    @Operation(summary = "Get staff by ID")
    @GetMapping("/{id}")
    public ResponseEntity<StaffDTO> get(
            @PathVariable Long id) {

        StaffDTO response = staffService.get(id);
        return ResponseEntity.ok()
                             .eTag(EtagUtils.toEtag(response.version()))
                             .body(response);
    }

    @Operation(summary = "Get all staff")
    @GetMapping
    public ResponseEntity<List<StaffDTO>> getAll() {

        List<StaffDTO> response = staffService.getAll();
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Create staff position")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<StaffDTO> create(@Valid @RequestBody StaffPostRequest staffPostRequest) {

        StaffDTO response = staffService.create(staffPostRequest);
        return ResponseEntity.created(LocationUtils.buildLocation(response.id()))
                             .eTag(EtagUtils.toEtag(response.version()))
                             .body(response);
    }

    @Operation(summary = "Replace staff position")
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<StaffDTO> replace(
            @PathVariable Long id,
            @Valid @RequestBody StaffPutRequest staffPutRequest,
            @RequestHeader(HttpHeaders.IF_MATCH) String ifMatch) {

        long expected = EtagUtils.parseIfMatch(ifMatch);
        StaffDTO response = staffService.replace(id, staffPutRequest, expected);
        return ResponseEntity.ok()
                             .eTag(EtagUtils.toEtag(response.version()))
                             .body(response);
    }

    @Operation(summary = "Partially update staff position")
    @PatchMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<StaffDTO> update(
            @PathVariable Long id,
            @Valid @RequestBody StaffPatchRequest staffPatchRequest,
            @RequestHeader(HttpHeaders.IF_MATCH) String ifMatch) {

        long expected = EtagUtils.parseIfMatch(ifMatch);
        StaffDTO response = staffService.update(id, staffPatchRequest, expected);
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
