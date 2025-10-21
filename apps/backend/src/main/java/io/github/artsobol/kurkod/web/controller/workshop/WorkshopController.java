package io.github.artsobol.kurkod.web.controller.workshop;

import io.github.artsobol.kurkod.common.constants.ApiLogMessage;
import io.github.artsobol.kurkod.common.util.LogUtils;
import io.github.artsobol.kurkod.web.domain.workshop.model.dto.WorkshopDTO;
import io.github.artsobol.kurkod.web.domain.workshop.model.request.WorkshopPatchRequest;
import io.github.artsobol.kurkod.web.domain.workshop.model.request.WorkshopPostRequest;
import io.github.artsobol.kurkod.web.domain.workshop.model.request.WorkshopPutRequest;
import io.github.artsobol.kurkod.web.domain.workshop.service.api.WorkshopService;
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
@Tag(name = "Workshops", description = "Workshops operations")
@RequestMapping(value = "/api/v1/workshops/", produces = "application/json")
public class WorkshopController {

    private final WorkshopService workshopService;

    @GetMapping("/{id}")
    @Operation(summary = "Get workshop by ID", description = "Returns a single workshop by its unique identifier.")
    public ResponseEntity<IamResponse<WorkshopDTO>> get(@Parameter(
            description = "Workshop identifier",
            example = "42"
    ) @PathVariable(name = "id") Integer id) {
        log.trace(ApiLogMessage.NAME_OF_CURRENT_METHOD.getValue(), LogUtils.getMethodName());
        WorkshopDTO response = workshopService.get(id);
        return ResponseEntity.ok(IamResponse.createSuccessful(response));
    }

    @GetMapping
    @Operation(summary = "Get all workshops", description = "Returns all workshops available in the system.")
    public ResponseEntity<IamResponse<List<WorkshopDTO>>> getAll() {
        log.trace(ApiLogMessage.NAME_OF_CURRENT_METHOD.getValue(), LogUtils.getMethodName());
        List<WorkshopDTO> response = workshopService.getAll();
        return ResponseEntity.ok(IamResponse.createSuccessful(response));
    }

    @PostMapping
    @Operation(summary = "Create a new workshop", description = "Creates a new workshop with the provided data.")
    public ResponseEntity<IamResponse<WorkshopDTO>> create(@RequestBody @Valid WorkshopPostRequest workshopPostRequest) {
        log.trace(ApiLogMessage.NAME_OF_CURRENT_METHOD.getValue(), LogUtils.getMethodName());
        WorkshopDTO response = workshopService.create(workshopPostRequest);
        return ResponseEntity.ok(IamResponse.createSuccessful(response));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Replace workshop by ID", description = "Replaces an existing workshop with new data.")
    public ResponseEntity<IamResponse<WorkshopDTO>> replace(
            @Parameter(description = "Workshop identifier", example = "42") @PathVariable(name = "id") Integer id,
            @RequestBody @Valid WorkshopPutRequest workshopPutRequest) {
        log.trace(ApiLogMessage.NAME_OF_CURRENT_METHOD.getValue(), LogUtils.getMethodName());
        WorkshopDTO response = workshopService.replace(id, workshopPutRequest);
        return ResponseEntity.ok(IamResponse.createSuccessful(response));
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Partially update workshop by ID", description = "Applies a partial update to an existing workshop by ID.")
    public ResponseEntity<IamResponse<WorkshopDTO>> update(
            @Parameter(description = "Workshop identifier", example = "42") @PathVariable(name = "id") Integer id,
            @RequestBody @Valid WorkshopPatchRequest workshopPatchRequest) {
        log.trace(ApiLogMessage.NAME_OF_CURRENT_METHOD.getValue(), LogUtils.getMethodName());
        WorkshopDTO response = workshopService.update(id, workshopPatchRequest);
        return ResponseEntity.ok(IamResponse.createSuccessful(response));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete workshop by ID", description = "Deletes an existing workshop by its unique identifier.")
    public ResponseEntity<Void> delete(
            @Parameter(description = "Workshop identifier", example = "42") @PathVariable(name = "id") Integer id) {
        log.trace(ApiLogMessage.NAME_OF_CURRENT_METHOD.getValue(), LogUtils.getMethodName());
        workshopService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
