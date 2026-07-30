package io.github.artsobol.kurkod.web.controller.worker;

import io.github.artsobol.kurkod.api.common.PageResponse;
import io.github.artsobol.kurkod.common.util.EtagUtils;
import io.github.artsobol.kurkod.web.domain.worker.model.dto.WorkerDTO;
import io.github.artsobol.kurkod.web.domain.worker.model.request.WorkerPatchRequest;
import io.github.artsobol.kurkod.web.domain.worker.model.request.WorkerPostRequest;
import io.github.artsobol.kurkod.web.domain.worker.model.request.WorkerPutRequest;
import io.github.artsobol.kurkod.web.domain.worker.service.api.WorkerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static io.github.artsobol.kurkod.common.util.LocationUtils.buildLocation;

@Validated
@RestController
@RequestMapping(value = "/api/v1/workers", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@Tag(name = "Workers", description = "Worker operations")
public class WorkerController {

    private final WorkerService workerService;

    @Operation(summary = "Get worker by ID")
    @GetMapping("/{id}")
    public ResponseEntity<WorkerDTO> get(
            @PathVariable Long id) {
        WorkerDTO response = workerService.get(id);
        return ResponseEntity.ok()
                             .eTag(EtagUtils.toEtag(response.version()))
                             .body(response);
    }

    @Operation(summary = "Get all workers")
    @GetMapping("/all")
    public ResponseEntity<List<WorkerDTO>> getAll() {
        List<WorkerDTO> response = workerService.getAll();
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get a page of workers")
    @GetMapping
    public PageResponse<WorkerDTO> getPage(
            @RequestParam(defaultValue = "0") @PositiveOrZero int page,
            @RequestParam(defaultValue = "10") @Positive @Max(100) int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Order.asc("id")));
        Page<WorkerDTO> response = workerService.getPage(pageable);
        return PageResponse.from(response);
    }

    @Operation(summary = "Create worker")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<WorkerDTO> create(@RequestBody @Valid WorkerPostRequest request) {
        WorkerDTO response = workerService.create(request);
        return ResponseEntity.created(buildLocation(response.id())).eTag(EtagUtils.toEtag(response.version())).body(
                response);
    }

    @Operation(summary = "Replace worker")
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<WorkerDTO> replace(
            @PathVariable Long id,
            @RequestBody @Valid WorkerPutRequest request,
            @RequestHeader(HttpHeaders.IF_MATCH) String ifMatch) {
        long expected = EtagUtils.parseIfMatch(ifMatch);
        WorkerDTO response = workerService.replace(id, request, expected);
        return ResponseEntity.ok()
                             .eTag(EtagUtils.toEtag(response.version()))
                             .body(response);
    }

    @Operation(summary = "Partially update worker")
    @PatchMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<WorkerDTO> update(
            @PathVariable Long id,
            @RequestBody @Valid WorkerPatchRequest request,
            @RequestHeader(HttpHeaders.IF_MATCH) String ifMatch) {
        long expected = EtagUtils.parseIfMatch(ifMatch);
        WorkerDTO response = workerService.update(id, request, expected);
        return ResponseEntity.ok()
                             .eTag(EtagUtils.toEtag(response.version()))
                             .body(response);
    }

    @Operation(summary = "Delete worker")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Long id,
            @RequestHeader(HttpHeaders.IF_MATCH) String ifMatch) {
        long expected = EtagUtils.parseIfMatch(ifMatch);
        workerService.delete(id, expected);
        return ResponseEntity.noContent().build();
    }
}
