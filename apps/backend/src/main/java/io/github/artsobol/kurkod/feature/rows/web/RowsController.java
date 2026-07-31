package io.github.artsobol.kurkod.feature.rows.web;

import io.github.artsobol.kurkod.infrastructure.util.EtagUtils;
import io.github.artsobol.kurkod.infrastructure.util.LocationUtils;
import io.github.artsobol.kurkod.feature.rows.dto.response.RowsDTO;
import io.github.artsobol.kurkod.feature.rows.dto.request.RowsUpdateRequest;
import io.github.artsobol.kurkod.feature.rows.dto.request.RowsCreateRequest;
import io.github.artsobol.kurkod.feature.rows.service.RowsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "Rows", description = "Rows operations")
@RequestMapping("/workshops/{workshopId}/rows")
public class RowsController {

    private final RowsService rowsService;

    @GetMapping("/{rowNumber}")
    @Operation(summary = "Get row by ID")
    public ResponseEntity<RowsDTO> get(
            @PathVariable Long workshopId,
            @PathVariable(name = "rowNumber")
            Integer rowsNumber) {

        RowsDTO response = rowsService.find(workshopId, rowsNumber);
        return ResponseEntity.ok()
                             .eTag(EtagUtils.toEtag(response.version()))
                             .body(response);
    }

    @GetMapping
    @Operation(summary = "Get all rows")
    public ResponseEntity<List<RowsDTO>> getAll(
            @PathVariable Long workshopId) {

        List<RowsDTO> response = rowsService.findAll(workshopId);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    @Operation(summary = "Create row")
    public ResponseEntity<RowsDTO> create(
            @PathVariable Long workshopId, @RequestBody @Valid RowsCreateRequest request) {

        RowsDTO response = rowsService.create(workshopId, request);
        return ResponseEntity.created(LocationUtils.buildLocation(response.rowNumber()))
                             .eTag(EtagUtils.toEtag(response.version()))
                             .body(response);
    }
    @PatchMapping("/{rowNumber}")
    @Operation(summary = "Partially update row")
    public ResponseEntity<RowsDTO> update(
            @PathVariable Long workshopId,
            @PathVariable(name = "rowNumber")
            Integer rowsNumber,
            @RequestBody @Valid RowsUpdateRequest request,
            @RequestHeader(HttpHeaders.IF_MATCH) String ifMatch) {

        long expected = EtagUtils.parseIfMatch(ifMatch);
        RowsDTO response = rowsService.update(workshopId, rowsNumber, request, expected);
        return ResponseEntity.ok()
                             .eTag(EtagUtils.toEtag(response.version()))
                             .body(response);
    }

    @DeleteMapping("/{rowNumber}")
    @Operation(summary = "Delete row")
    public ResponseEntity<Void> delete(
            @PathVariable Long workshopId,
            @PathVariable(name = "rowNumber")
            Integer rowsNumber,
            @RequestHeader(HttpHeaders.IF_MATCH) String ifMatch) {

        long expected = EtagUtils.parseIfMatch(ifMatch);
        rowsService.delete(workshopId, rowsNumber, expected);
        return ResponseEntity.noContent().build();
    }

}
