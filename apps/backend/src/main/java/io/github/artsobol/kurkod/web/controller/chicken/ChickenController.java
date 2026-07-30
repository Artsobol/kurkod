package io.github.artsobol.kurkod.web.controller.chicken;

import io.github.artsobol.kurkod.common.constants.ApiLogMessage;
import io.github.artsobol.kurkod.common.util.EtagUtils;
import io.github.artsobol.kurkod.common.util.LocationUtils;
import io.github.artsobol.kurkod.common.util.LogUtils;
import io.github.artsobol.kurkod.web.domain.chicken.model.dto.ChickenDTO;
import io.github.artsobol.kurkod.web.domain.chicken.model.request.ChickenPatchRequest;
import io.github.artsobol.kurkod.web.domain.chicken.model.request.ChickenPutRequest;
import io.github.artsobol.kurkod.web.domain.chicken.model.request.ChickenPostRequest;
import io.github.artsobol.kurkod.web.domain.chicken.service.api.ChickenService;
import io.github.artsobol.kurkod.web.response.PaginationResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
    public ResponseEntity<ChickenDTO> create(@Valid @RequestBody ChickenPostRequest request) {
        log.trace(ApiLogMessage.NAME_OF_CURRENT_METHOD.getValue(), LogUtils.getMethodName());
        ChickenDTO response = chickenService.create(request);
        return ResponseEntity.created(LocationUtils.buildLocation(response.id()))
                             .eTag(EtagUtils.toEtag(response.version()))
                             .body(response);
    }

    @Operation(summary = "List all chickens", description = "Returns all chickens.")
    @GetMapping("/all")
    public ResponseEntity<List<ChickenDTO>> getAll() {
        log.trace(ApiLogMessage.NAME_OF_CURRENT_METHOD.getValue(), LogUtils.getMethodName());
        List<ChickenDTO> response = chickenService.getAll();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Operation(summary = "List all breeds with pagination", description = "Returns all breeds with pagination.")
    @GetMapping
    public ResponseEntity<PaginationResponse<ChickenDTO>> getAllPaginated(
            @Parameter(description = "Page number. The first page has index 0", example = "0")
            @RequestParam(name = "page", defaultValue = "0") int page,
            @Parameter(description = "Number of items per page", example = "10")
            @RequestParam(name = "limit", defaultValue = "10") int limit) {
        log.trace(ApiLogMessage.NAME_OF_CURRENT_METHOD.getValue(), LogUtils.getMethodName());
        Pageable pageable = PageRequest.of(page, limit);
        Page<ChickenDTO> response = chickenService.getAllWithPagination(pageable);
        return ResponseEntity.ok(PaginationResponse.fromPage(response));
    }

    @Operation(summary = "Get chicken by ID", description = "Returns a single chicken by its unique identifier.")
    @GetMapping("/{id}")
    public ResponseEntity<ChickenDTO> get(
            @PathVariable @Parameter(description = "Chicken identifier", example = "42") Long id) {
        log.trace(ApiLogMessage.NAME_OF_CURRENT_METHOD.getValue(), LogUtils.getMethodName());
        ChickenDTO response = chickenService.get(id);
        return ResponseEntity.status(HttpStatus.OK)
                             .eTag(EtagUtils.toEtag(response.version()))
                             .body(response);
    }

    @Operation(summary = "Replace a chicken", description = "Fully replaces a chicken by ID.")
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ChickenDTO> replace(
            @PathVariable @Parameter(description = "Chicken identifier", example = "42") Long id,
            @Valid @RequestBody ChickenPutRequest request,
            @Parameter(name = "If-Match",
                       in = ParameterIn.HEADER,
                       required = true,
                       description = "ETag of the resource") @RequestHeader(value = "If-Match", required = false)
            String ifMatch) {
        log.trace(ApiLogMessage.NAME_OF_CURRENT_METHOD.getValue(), LogUtils.getMethodName());
        long expected = EtagUtils.parseIfMatch(ifMatch);
        ChickenDTO response = chickenService.replace(id, request, expected);
        return ResponseEntity.status(HttpStatus.OK)
                             .eTag(EtagUtils.toEtag(response.version()))
                             .body(response);
    }

    @Operation(summary = "Partially update a chicken", description = "Applies a partial update to a chicken by ID.")
    @PatchMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ChickenDTO> update(
            @PathVariable @Parameter(description = "Chicken identifier", example = "42") Long id,
            @Valid @RequestBody ChickenPatchRequest request,
            @Parameter(name = "If-Match",
                       in = ParameterIn.HEADER,
                       required = true,
                       description = "ETag of the resource") @RequestHeader(value = "If-Match", required = false)
            String ifMatch) {
        log.trace(ApiLogMessage.NAME_OF_CURRENT_METHOD.getValue(), LogUtils.getMethodName());
        long expected = EtagUtils.parseIfMatch(ifMatch);
        ChickenDTO response = chickenService.update(id, request, expected);
        return ResponseEntity.status(HttpStatus.OK)
                             .eTag(EtagUtils.toEtag(response.version()))
                             .body(response);
    }

    @Operation(summary = "Delete a chicken", description = "Deletes a chicken by its unique identifier.")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable @Parameter(description = "Chicken identifier", example = "42") Long id,
            @Parameter(name = "If-Match",
                       in = ParameterIn.HEADER,
                       required = true,
                       description = "ETag of the resource") @RequestHeader(value = "If-Match", required = false)
            String ifMatch) {
        log.trace(ApiLogMessage.NAME_OF_CURRENT_METHOD.getValue(), LogUtils.getMethodName());
        long expected = EtagUtils.parseIfMatch(ifMatch);
        chickenService.delete(id, expected);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).eTag(ifMatch).build();
    }
}
