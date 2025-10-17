package io.github.artsobol.kurkod.controller;

import io.github.artsobol.kurkod.model.constants.ApiLogMessage;
import io.github.artsobol.kurkod.model.dto.chicken.ChickenDTO;
import io.github.artsobol.kurkod.model.request.chicken.ChickenPatchRequest;
import io.github.artsobol.kurkod.model.request.chicken.ChickenPutRequest;
import io.github.artsobol.kurkod.model.request.chicken.ChickenPostRequest;
import io.github.artsobol.kurkod.model.response.IamResponse;
import io.github.artsobol.kurkod.service.ChickenService;
import io.github.artsobol.kurkod.utils.ApiUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(value = "/api/v1/chickens", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@Tag(name = "Chickens", description = "Chicken operations")
public class ChickenController {

    private final ChickenService chickenService;

    @Operation(summary = "Create a chicken", description = "Creates a new chicken entity.")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<IamResponse<ChickenDTO>> createChicken(@Valid @RequestBody ChickenPostRequest chickenPostRequest) {
        log.trace(ApiLogMessage.NAME_OF_CURRENT_METHOD.getValue(), ApiUtils.getMethodName());

        IamResponse<ChickenDTO> response = chickenService.createChicken(chickenPostRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "List all chickens", description = "Returns all chickens.")
    @GetMapping
    public ResponseEntity<IamResponse<List<ChickenDTO>>> getAllChickens() {
        log.trace(ApiLogMessage.NAME_OF_CURRENT_METHOD.getValue(), ApiUtils.getMethodName());

        IamResponse<List<ChickenDTO>> response = chickenService.getAll();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Operation(summary = "Get chicken by ID", description = "Returns a single chicken by its unique identifier.")
    @GetMapping("/{id}")
    public ResponseEntity<IamResponse<ChickenDTO>> getChickenById(@Parameter(
            description = "Chicken identifier",
            example = "42"
    ) @PathVariable(name = "id") Integer id) {
        log.trace(ApiLogMessage.NAME_OF_CURRENT_METHOD.getValue(), ApiUtils.getMethodName());

        IamResponse<ChickenDTO> response = chickenService.getById(id);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }


    @Operation(summary = "Replace a chicken", description = "Fully replaces a chicken by ID.")
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<IamResponse<ChickenDTO>> updateFully(@Parameter(
            description = "Chicken identifier",
            example = "42"
    ) @PathVariable(name = "id") Integer id, @Valid @RequestBody ChickenPutRequest chickenPutRequest) {
        log.trace(ApiLogMessage.NAME_OF_CURRENT_METHOD.getValue(), ApiUtils.getMethodName());

        IamResponse<ChickenDTO> response = chickenService.updateFully(id, chickenPutRequest);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Operation(summary = "Partially update a chicken", description = "Applies a partial update to a chicken by ID.")
    @PatchMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<IamResponse<ChickenDTO>> updatePartially(@Parameter(
            description = "Chicken identifier",
            example = "42"
    ) @PathVariable(name = "id") Integer id, @Valid @RequestBody ChickenPatchRequest chickenPatchRequest) {
        log.trace(ApiLogMessage.NAME_OF_CURRENT_METHOD.getValue(), ApiUtils.getMethodName());

        IamResponse<ChickenDTO> response = chickenService.updatePartially(id, chickenPatchRequest);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Operation(summary = "Delete a chicken", description = "Deletes a chicken by its unique identifier.")
    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteById(@Parameter(
            description = "Chicken identifier",
            example = "42"
    ) @PathVariable(name = "id") Integer id) {
        log.trace(ApiLogMessage.NAME_OF_CURRENT_METHOD.getValue(), ApiUtils.getMethodName());

        chickenService.deleteById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
