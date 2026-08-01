package io.github.artsobol.kurkod.feature.dismissal.web;

import io.github.artsobol.kurkod.infrastructure.utils.EtagUtils;
import io.github.artsobol.kurkod.infrastructure.utils.LocationUtils;
import io.github.artsobol.kurkod.infrastructure.security.user.UserPrincipal;
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
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/dismissals")
@RequiredArgsConstructor
@Tag(name = "Dismissals", description = "Dismissals API")
public class DismissalController {

  private final DismissalService dismissalService;

  @GetMapping("/workers/{workerId}/dismissed/{dismissedId}")
  @Operation(summary = "Get dismissal by worker and dismissed")
  public ResponseEntity<DismissalResponse> getByWorkerAndDismissed(
      @PathVariable Long workerId, @PathVariable Long dismissedId) {

    DismissalResponse response = dismissalService.getByWorkerAndDismissed(workerId, dismissedId);
    return ResponseEntity.ok().eTag(EtagUtils.toEtag(response.version())).body(response);
  }

  @GetMapping("/dismissed/{dismissedId}")
  @Operation(summary = "Get dismissals by dismissed")
  public ResponseEntity<List<DismissalResponse>> getAllByDismissed(@PathVariable Long dismissedId) {

    List<DismissalResponse> response = dismissalService.getAllByDismissed(dismissedId);
    return ResponseEntity.ok(response);
  }

  @GetMapping("/workers/{workerId}")
  @Operation(summary = "Get dismissals by worker")
  public ResponseEntity<List<DismissalResponse>> getAllByWorker(@PathVariable Long workerId) {

    List<DismissalResponse> response = dismissalService.getAllByWorker(workerId);
    return ResponseEntity.ok(response);
  }

  @PostMapping
  @Operation(summary = "Create dismissal")
  public ResponseEntity<DismissalResponse> create(
      @RequestBody @Valid DismissalCreateRequest request,
      @AuthenticationPrincipal UserPrincipal principal) {

    DismissalResponse response = dismissalService.create(request, principal.userId());
    return ResponseEntity.created(
            LocationUtils.buildLocation(
                "/dismissals/workers/{workerId}/dismissed/{dismissedId}",
                request.getWorkerId(),
                principal.userId()))
        .eTag(EtagUtils.toEtag(response.version()))
        .body(response);
  }

  @PatchMapping("/{workerId}")
  @Operation(summary = "Update dismissal by worker id")
  public ResponseEntity<DismissalResponse> update(
      @PathVariable Long workerId,
      @RequestBody @Valid DismissalUpdateRequest request,
      @RequestHeader(value = HttpHeaders.IF_MATCH, required = false) String ifMatch) {

    long expected = EtagUtils.parseIfMatch(ifMatch);
    DismissalResponse response = dismissalService.update(workerId, request, expected);
    return ResponseEntity.ok().eTag(EtagUtils.toEtag(response.version())).body(response);
  }
}
