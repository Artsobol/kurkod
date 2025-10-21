package io.github.artsobol.kurkod.web.controller.rows;

import io.github.artsobol.kurkod.common.constants.ApiLogMessage;
import io.github.artsobol.kurkod.common.util.LogUtils;
import io.github.artsobol.kurkod.web.domain.rows.model.dto.RowsDTO;
import io.github.artsobol.kurkod.web.domain.rows.model.request.RowsPatchRequest;
import io.github.artsobol.kurkod.web.domain.rows.model.request.RowsPostRequest;
import io.github.artsobol.kurkod.web.domain.rows.model.request.RowsPutRequest;
import io.github.artsobol.kurkod.web.domain.rows.service.api.RowsService;
import io.github.artsobol.kurkod.web.response.IamResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "Rows", description = "Rows operations")
@RequestMapping("/api/v1/workshops/{workshopId}/rows")
public class RowsController {

    private final RowsService rowsService;

    @GetMapping("/{rowNumber}")
    @Operation(summary = "Get row by ID", description = "Returns a single row by its unique identifier.")
    public ResponseEntity<IamResponse<RowsDTO>> get(@Parameter(
            description = "Workshop identifier",
            example = "1"
    ) @PathVariable(name = "workshopId") Integer workshopId, @Parameter(
            description = "Row identifier",
            example = "2"
    ) @PathVariable(name = "rowNumber") Integer rowsNumber) {
        log.trace(ApiLogMessage.NAME_OF_CURRENT_METHOD.getValue(), LogUtils.getMethodName());
        RowsDTO response = rowsService.find(workshopId, rowsNumber);
        return ResponseEntity.ok(IamResponse.createSuccessful(response));
    }

    @GetMapping
    @Operation(summary = "Get all rows", description = "Returns all rows available in the system.")
    public ResponseEntity<IamResponse<List<RowsDTO>>> getAll(@Parameter(
            description = "Workshop identifier",
            example = "1"
    ) @PathVariable(name = "workshopId") Integer workshopId) {
        log.trace(ApiLogMessage.NAME_OF_CURRENT_METHOD.getValue(), LogUtils.getMethodName());
        List<RowsDTO> response = rowsService.findAll(workshopId);
        return ResponseEntity.ok(IamResponse.createSuccessful(response));
    }

    @PostMapping
    @Operation(summary = "Create a new row", description = "Creates a new row in the system.")
    public ResponseEntity<IamResponse<RowsDTO>> create(@Parameter(
            description = "Workshop identifier",
            example = "1"
    ) @PathVariable(name = "workshopId") Integer workshopId, @RequestBody @Valid RowsPostRequest rowsPostRequest) {
        log.trace(ApiLogMessage.NAME_OF_CURRENT_METHOD.getValue(), LogUtils.getMethodName());
        RowsDTO response = rowsService.create(workshopId, rowsPostRequest);
        return ResponseEntity.ok(IamResponse.createSuccessful(response));
    }

    @PutMapping("/{rowNumber}")
    @Operation(summary = "Replace row by ID", description = "Replaces an existing row with new data.")
    public ResponseEntity<IamResponse<RowsDTO>> replace(@Parameter(
            description = "Workshop identifier",
            example = "1"
    ) @PathVariable(name = "workshopId") Integer workshopId, @Parameter(
            description = "Row identifier",
            example = "2"
    ) @PathVariable(name = "rowNumber") Integer rowsNumber, @RequestBody @Valid RowsPutRequest rowsPutRequest) {
        log.trace(ApiLogMessage.NAME_OF_CURRENT_METHOD.getValue(), LogUtils.getMethodName());
        RowsDTO response = rowsService.replace(workshopId, rowsNumber, rowsPutRequest);
        return ResponseEntity.ok(IamResponse.createSuccessful(response));
    }

    @PatchMapping("/{rowNumber}")
    @Operation(
            summary = "Partially update row by ID",
            description = "Applies a partial update to an existing row by ID."
    )
    public ResponseEntity<IamResponse<RowsDTO>> update(@Parameter(
            description = "Workshop identifier",
            example = "1"
    ) @PathVariable(name = "workshopId") Integer workshopId, @Parameter(
            description = "Row identifier",
            example = "2"
    ) @PathVariable(name = "rowNumber") Integer rowsNumber, @RequestBody @Valid RowsPatchRequest rowsPatchRequest) {
        log.trace(ApiLogMessage.NAME_OF_CURRENT_METHOD.getValue(), LogUtils.getMethodName());
        RowsDTO response = rowsService.update(workshopId, rowsNumber, rowsPatchRequest);
        return ResponseEntity.ok(IamResponse.createSuccessful(response));
    }

    @DeleteMapping("/{rowNumber}")
    @Operation(summary = "Delete row by ID", description = "Deletes a row by its unique identifier.")
    public ResponseEntity<Void> delete(@Parameter(
            description = "Workshop identifier",
            example = "1"
    ) @PathVariable(name = "workshopId") Integer workshopId, @Parameter(
            description = "Row identifier",
            example = "2"
    ) @PathVariable(name = "rowNumber") Integer rowsNumber) {
        log.trace(ApiLogMessage.NAME_OF_CURRENT_METHOD.getValue(), LogUtils.getMethodName());
        rowsService.delete(workshopId, rowsNumber);
        return ResponseEntity.noContent().build();
    }

}
