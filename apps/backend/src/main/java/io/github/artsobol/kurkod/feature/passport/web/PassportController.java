package io.github.artsobol.kurkod.feature.passport.web;

import io.github.artsobol.kurkod.infrastructure.util.EtagUtils;
import io.github.artsobol.kurkod.infrastructure.util.LocationUtils;
import io.github.artsobol.kurkod.feature.passport.dto.response.PassportDTO;
import io.github.artsobol.kurkod.feature.passport.dto.request.PassportPatchRequest;
import io.github.artsobol.kurkod.feature.passport.dto.request.PassportPostRequest;
import io.github.artsobol.kurkod.feature.passport.dto.request.PassportPutRequest;
import io.github.artsobol.kurkod.feature.passport.service.PassportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/workers/{workerId}/passport", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Passport", description = "Passport operations")
public class PassportController {

    private final PassportService passportService;

    @Operation(summary = "Get passport by worker ID")
    @GetMapping
    public ResponseEntity<PassportDTO> get(
            @PathVariable(name = "workerId") Long id) {

        PassportDTO response = passportService.get(id);
        return ResponseEntity.ok()
                             .eTag(EtagUtils.toEtag(response.version()))
                             .body(response);
    }

    @Operation(summary = "Create passport for a worker")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PassportDTO> create(
            @PathVariable(name = "workerId") Long id,
            @RequestBody @Valid PassportPostRequest request) {

        PassportDTO response = passportService.create(id, request);
        return ResponseEntity.created(LocationUtils.buildLocation()).body(response);
    }

    @Operation(summary = "Replace worker’s passport")
    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PassportDTO> replace(
            @PathVariable(name = "workerId") Long id,
            @RequestBody @Valid PassportPutRequest request,
            @RequestHeader(HttpHeaders.IF_MATCH) String ifMatch) {

        long expected = EtagUtils.parseIfMatch(ifMatch);
        PassportDTO response = passportService.replace(id, request, expected);
        return ResponseEntity.ok()
                             .eTag(EtagUtils.toEtag(response.version()))
                             .body(response);
    }

    @Operation(summary = "Partially update worker’s passport")
    @PatchMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PassportDTO> update(
            @PathVariable(name = "workerId") Long id,
            @RequestBody @Valid PassportPatchRequest request,
            @RequestHeader(HttpHeaders.IF_MATCH) String ifMatch) {

        long expected = EtagUtils.parseIfMatch(ifMatch);
        PassportDTO response = passportService.update(id, request, expected);
        return ResponseEntity.ok()
                             .eTag(EtagUtils.toEtag(response.version()))
                             .body(response);
    }

    @Operation(summary = "Delete worker’s passport")
    @DeleteMapping
    public ResponseEntity<Void> delete(
            @PathVariable(name = "workerId") Long id,
            @RequestHeader(HttpHeaders.IF_MATCH) String ifMatch) {

        long expected = EtagUtils.parseIfMatch(ifMatch);
        passportService.delete(id, expected);
        return ResponseEntity.noContent().build();
    }
}
