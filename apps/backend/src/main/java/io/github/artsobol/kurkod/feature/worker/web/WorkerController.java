package io.github.artsobol.kurkod.feature.worker.web;

import static io.github.artsobol.kurkod.infrastructure.utils.LocationUtils.buildLocation;

import io.github.artsobol.kurkod.feature.worker.dto.request.WorkerCreateRequest;
import io.github.artsobol.kurkod.feature.worker.dto.request.WorkerUpdateRequest;
import io.github.artsobol.kurkod.feature.worker.dto.response.WorkerResponse;
import io.github.artsobol.kurkod.feature.worker.service.WorkerService;
import io.github.artsobol.kurkod.infrastructure.utils.EtagUtils;
import io.github.artsobol.kurkod.infrastructure.web.dto.PageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import java.util.List;
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

@Validated
@RestController
@RequestMapping(value = "/workers", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@Tag(name = "Workers", description = "Worker operations")
public class WorkerController {

    private final WorkerService workerService;

    @Operation(summary = "Get worker by ID")
    @GetMapping("/{id}")
    public ResponseEntity<WorkerResponse> get(
            @PathVariable Long id) {
        WorkerResponse response = workerService.get(id);
        return ResponseEntity.ok()
                             .eTag(EtagUtils.toEtag(response.version()))
                             .body(response);
    }

    @Operation(summary = "Get all workers")
    @GetMapping("/all")
    public ResponseEntity<List<WorkerResponse>> getAll() {
        List<WorkerResponse> response = workerService.getAll();
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get a page of workers")
    @GetMapping
    public PageResponse<WorkerResponse> getPage(
            @RequestParam(defaultValue = "0") @PositiveOrZero int page,
            @RequestParam(defaultValue = "10") @Positive @Max(100) int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Order.asc("id")));
        Page<WorkerResponse> response = workerService.getPage(pageable);
        return PageResponse.from(response);
    }

    @Operation(summary = "Create worker")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<WorkerResponse> create(@RequestBody @Valid WorkerCreateRequest request) {
        WorkerResponse response = workerService.create(request);
        return ResponseEntity.created(buildLocation(response.id())).eTag(EtagUtils.toEtag(response.version())).body(
                response);
    }
    @Operation(summary = "Partially update worker")
    @PatchMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<WorkerResponse> update(
            @PathVariable Long id,
            @RequestBody @Valid WorkerUpdateRequest request,
            @RequestHeader(value = HttpHeaders.IF_MATCH, required = false) String ifMatch) {
        long expected = EtagUtils.parseIfMatch(ifMatch);
        WorkerResponse response = workerService.update(id, request, expected);
        return ResponseEntity.ok()
                             .eTag(EtagUtils.toEtag(response.version()))
                             .body(response);
    }

    @Operation(summary = "Delete worker")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Long id,
            @RequestHeader(value = HttpHeaders.IF_MATCH, required = false) String ifMatch) {
        long expected = EtagUtils.parseIfMatch(ifMatch);
        workerService.delete(id, expected);
        return ResponseEntity.noContent().build();
    }
}
