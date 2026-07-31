package io.github.artsobol.kurkod.feature.dismissal.web;

import io.github.artsobol.kurkod.infrastructure.util.EtagUtils;
import io.github.artsobol.kurkod.infrastructure.util.LocationUtils;
import io.github.artsobol.kurkod.infrastructure.security.facade.SecurityContextFacade;
import io.github.artsobol.kurkod.feature.dismissal.dto.response.DismissalResponse;
import io.github.artsobol.kurkod.feature.dismissal.dto.request.DismissalUpdateRequest;
import io.github.artsobol.kurkod.feature.dismissal.dto.request.DismissalCreateRequest;
import io.github.artsobol.kurkod.feature.dismissal.service.DismissalService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/dismissals")
@RequiredArgsConstructor
@Tag(name = "Dismissals", description = "Dismissals API")
public class DismissalController {

    private final DismissalService dismissalService;
    private final SecurityContextFacade securityContextFacade;

    @GetMapping("/workers/{workerId}/dismissed/{dismissedId}")
    @Operation(summary = "Get dismissal by worker and dismissed")
    public ResponseEntity<DismissalResponse> getByWorkerAndDismissed(
            @PathVariable Long workerId,
            @PathVariable Long dismissedId) {

        DismissalResponse response = dismissalService.getByWorkerAndDismissed(workerId, dismissedId);
        return ResponseEntity.ok()
                             .eTag(EtagUtils.toEtag(response.version()))
                             .body(response);
    }

    @GetMapping("/dismissed/{dismissedId}")
    @Operation(summary = "Get dismissals by dismissed")
    public ResponseEntity<List<DismissalResponse>> getAllByDismissed(
            @PathVariable Long dismissedId) {

        List<DismissalResponse> response = dismissalService.getAllByDismissed(dismissedId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/workers/{workerId}")
    @Operation(summary = "Get dismissals by worker")
    public ResponseEntity<List<DismissalResponse>> getAllByWorker(
            @PathVariable Long workerId) {

        List<DismissalResponse> response = dismissalService.getAllByWorker(workerId);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    @Operation(summary = "Create dismissal")
    public ResponseEntity<DismissalResponse> create(
            @RequestBody @Valid DismissalCreateRequest request) {

        DismissalResponse response = dismissalService.create(request);
        return ResponseEntity.created(LocationUtils.buildLocation(request.getWorkerId(),
                                                                  securityContextFacade.getCurrentUserId())).eTag(
                EtagUtils.toEtag(response.version())).body(response);
    }
    @PatchMapping("/{workerId}")
    @Operation(summary = "Update dismissal by worker id")
    public ResponseEntity<DismissalResponse> update(
            @PathVariable Long workerId,
            @RequestBody @Valid DismissalUpdateRequest request,
            @RequestHeader(HttpHeaders.IF_MATCH) String ifMatch) {

        long expected = EtagUtils.parseIfMatch(ifMatch);
        DismissalResponse response = dismissalService.update(workerId, request, expected);
        return ResponseEntity.ok()
                             .eTag(EtagUtils.toEtag(response.version()))
                             .body(response);
    }

}
