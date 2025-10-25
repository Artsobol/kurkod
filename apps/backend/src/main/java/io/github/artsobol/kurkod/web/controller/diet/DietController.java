package io.github.artsobol.kurkod.web.controller.diet;

import io.github.artsobol.kurkod.common.constants.ApiLogMessage;
import io.github.artsobol.kurkod.common.util.LogUtils;
import io.github.artsobol.kurkod.web.domain.diet.model.dto.DietDTO;
import io.github.artsobol.kurkod.web.domain.diet.model.request.DietPatchRequest;
import io.github.artsobol.kurkod.web.domain.diet.model.request.DietPostRequest;
import io.github.artsobol.kurkod.web.domain.diet.model.request.DietPutRequest;
import io.github.artsobol.kurkod.web.domain.diet.service.api.DietService;
import io.github.artsobol.kurkod.web.response.IamResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@Tag(name = "Diet", description = "Diet API")
@RequiredArgsConstructor
@RequestMapping("/api/v1/diets")
public class DietController {

    private final DietService dietService;

    @GetMapping("/{id}")
    @Operation(summary = "Get diet by ID", description = "Returns a single diet by its unique identifier.")
    public ResponseEntity<IamResponse<DietDTO>> get(@Parameter(name="Id",  example = "1") @PathVariable(name="id") Integer id) {
        log.trace(ApiLogMessage.NAME_OF_CURRENT_METHOD.getValue(), LogUtils.getMethodName());
        DietDTO response = dietService.get(id);
        return ResponseEntity.ok(IamResponse.createSuccessful(response));
    }

    @GetMapping
    @Operation(summary = "List all diets", description = "Returns all diets available in the system.")
    public ResponseEntity<IamResponse<Iterable<DietDTO>>> getAll() {
        log.trace(ApiLogMessage.NAME_OF_CURRENT_METHOD.getValue(), LogUtils.getMethodName());
        Iterable<DietDTO> response = dietService.getAll();
        return ResponseEntity.ok(IamResponse.createSuccessful(response));
    }

    @PostMapping
    @Operation(summary = "Create a new diet", description = "Creates a new diet with the provided data.")
    public ResponseEntity<IamResponse<DietDTO>> create(@RequestBody @Valid DietPostRequest dietPostRequest) {
        log.trace(ApiLogMessage.NAME_OF_CURRENT_METHOD.getValue(), LogUtils.getMethodName());
        DietDTO response = dietService.create(dietPostRequest);
        return ResponseEntity.ok(IamResponse.createSuccessful(response));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Replace diet by ID", description = "Replaces an existing diet with new data.")
    public ResponseEntity<IamResponse<DietDTO>> replace(
            @Parameter(name="Id",  example = "1") @PathVariable(name="id") Integer id,
            @RequestBody @Valid DietPutRequest dietPutRequest) {
        log.trace(ApiLogMessage.NAME_OF_CURRENT_METHOD.getValue(), LogUtils.getMethodName());
        DietDTO response = dietService.replace(id, dietPutRequest);
        return ResponseEntity.ok(IamResponse.createSuccessful(response));
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Partially update diet by ID", description = "Applies a partial update to an existing diet by ID.")
    public ResponseEntity<IamResponse<DietDTO>> update(
            @Parameter(name="Id",  example = "1") @PathVariable(name="id") Integer id,
            @RequestBody @Valid DietPatchRequest dietPatchRequest) {
        log.trace(ApiLogMessage.NAME_OF_CURRENT_METHOD.getValue(), LogUtils.getMethodName());
        DietDTO response = dietService.update(id, dietPatchRequest);
        return ResponseEntity.ok(IamResponse.createSuccessful(response));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete diet by ID", description = "Deletes an existing diet by its unique identifier.")
    public ResponseEntity<Void> delete(@Parameter(name="Id",  example = "1") @PathVariable(name="id") Integer id) {
        log.trace(ApiLogMessage.NAME_OF_CURRENT_METHOD.getValue(), LogUtils.getMethodName());
        dietService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
