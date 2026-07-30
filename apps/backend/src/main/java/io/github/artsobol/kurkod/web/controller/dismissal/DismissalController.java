package io.github.artsobol.kurkod.web.controller.dismissal;

import io.github.artsobol.kurkod.common.util.EtagUtils;
import io.github.artsobol.kurkod.common.util.LocationUtils;
import io.github.artsobol.kurkod.security.facade.SecurityContextFacade;
import io.github.artsobol.kurkod.web.domain.dismissal.model.dto.DismissalDTO;
import io.github.artsobol.kurkod.web.domain.dismissal.model.request.DismissalPatchRequest;
import io.github.artsobol.kurkod.web.domain.dismissal.model.request.DismissalPostRequest;
import io.github.artsobol.kurkod.web.domain.dismissal.model.request.DismissalPutRequest;
import io.github.artsobol.kurkod.web.domain.dismissal.service.api.DismissalService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/dismissals")
@RequiredArgsConstructor
@Tag(name = "Dismissals", description = "Dismissals API")
public class DismissalController {

    private final DismissalService dismissalService;
    private final SecurityContextFacade securityContextFacade;

    @GetMapping("/workers/{workerId}/dismissed/{dismissedId}")
    @Operation(summary = "Get dismissal by worker and dismissed")
    public ResponseEntity<DismissalDTO> getByWorkerAndDismissed(
            @PathVariable Long workerId,
            @PathVariable Long dismissedId) {

        DismissalDTO response = dismissalService.getByWorkerAndDismissed(workerId, dismissedId);
        return ResponseEntity.ok()
                             .eTag(EtagUtils.toEtag(response.version()))
                             .body(response);
    }

    @GetMapping("/dismissed/{dismissedId}")
    @Operation(summary = "Get dismissals by dismissed")
    public ResponseEntity<List<DismissalDTO>> getAllByDismissed(
            @PathVariable Long dismissedId) {

        List<DismissalDTO> response = dismissalService.getAllByDismissed(dismissedId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/workers/{workerId}")
    @Operation(summary = "Get dismissals by worker")
    public ResponseEntity<List<DismissalDTO>> getAllByWorker(
            @PathVariable Long workerId) {

        List<DismissalDTO> response = dismissalService.getAllByWorker(workerId);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    @Operation(summary = "Create dismissal")
    public ResponseEntity<DismissalDTO> create(
            @RequestBody @Valid DismissalPostRequest request) {

        DismissalDTO response = dismissalService.create(request);
        return ResponseEntity.created(LocationUtils.buildLocation(request.getWorkerId(),
                                                                  securityContextFacade.getCurrentUserId())).eTag(
                EtagUtils.toEtag(response.version())).body(response);
    }

    @PutMapping("/{workerId}")
    @Operation(summary = "Replace dismissal by worker id")
    public ResponseEntity<DismissalDTO> replace(
            @PathVariable Long workerId,
            @RequestBody @Valid DismissalPutRequest request,
            @RequestHeader(HttpHeaders.IF_MATCH) String ifMatch) {

        long expected = EtagUtils.parseIfMatch(ifMatch);
        DismissalDTO response = dismissalService.replace(workerId, request, expected);
        return ResponseEntity.ok()
                             .eTag(EtagUtils.toEtag(response.version()))
                             .body(response);
    }

    @PatchMapping("/{workerId}")
    @Operation(summary = "Update dismissal by worker id")
    public ResponseEntity<DismissalDTO> update(
            @PathVariable Long workerId,
            @RequestBody @Valid DismissalPatchRequest request,
            @RequestHeader(HttpHeaders.IF_MATCH) String ifMatch) {

        long expected = EtagUtils.parseIfMatch(ifMatch);
        DismissalDTO response = dismissalService.update(workerId, request, expected);
        return ResponseEntity.ok()
                             .eTag(EtagUtils.toEtag(response.version()))
                             .body(response);
    }

}
