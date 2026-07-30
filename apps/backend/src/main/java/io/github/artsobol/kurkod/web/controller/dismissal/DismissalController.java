package io.github.artsobol.kurkod.web.controller.dismissal;

import io.github.artsobol.kurkod.common.constants.ApiLogMessage;
import io.github.artsobol.kurkod.common.util.EtagUtils;
import io.github.artsobol.kurkod.common.util.LocationUtils;
import io.github.artsobol.kurkod.common.util.LogUtils;
import io.github.artsobol.kurkod.security.facade.SecurityContextFacade;
import io.github.artsobol.kurkod.web.domain.dismissal.model.dto.DismissalDTO;
import io.github.artsobol.kurkod.web.domain.dismissal.model.request.DismissalPatchRequest;
import io.github.artsobol.kurkod.web.domain.dismissal.model.request.DismissalPostRequest;
import io.github.artsobol.kurkod.web.domain.dismissal.model.request.DismissalPutRequest;
import io.github.artsobol.kurkod.web.domain.dismissal.service.api.DismissalService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/dismissals")
@RequiredArgsConstructor
@Tag(name = "Dismissals", description = "Dismissals API")
public class DismissalController {

    private final DismissalService dismissalService;
    private final SecurityContextFacade securityContextFacade;

    @GetMapping("/workers/{workerId}/dismissed/{dismissedId}")
    @Operation(summary = "Get dismissal by worker and dismissed",
               description = "Returns a single dismissal by worker and dismissed.")
    public ResponseEntity<DismissalDTO> getByWorkerAndDismissed(
            @PathVariable @Parameter(name = "worker id", example = "1") Long workerId,
            @PathVariable @Parameter(name = "dismissed id", example = "1") Long dismissedId) {
        log.trace(ApiLogMessage.NAME_OF_CURRENT_METHOD.getValue(), LogUtils.getMethodName());
        DismissalDTO response = dismissalService.getByWorkerAndDismissed(workerId, dismissedId);
        return ResponseEntity.status(HttpStatus.OK)
                             .eTag(EtagUtils.toEtag(response.version()))
                             .body(response);
    }

    @GetMapping("/dismissed/{dismissedId}")
    @Operation(summary = "Get dismissals by dismissed", description = "Returns all dismissals by dismissed.")
    public ResponseEntity<List<DismissalDTO>> getAllByDismissed(
            @PathVariable @Parameter(name = "dismissed id", example = "1") Long dismissedId) {
        log.trace(ApiLogMessage.NAME_OF_CURRENT_METHOD.getValue(), LogUtils.getMethodName());
        List<DismissalDTO> response = dismissalService.getAllByDismissed(dismissedId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/workers/{workerId}")
    @Operation(summary = "Get dismissals by worker", description = "Returns all dismissals by worker.")
    public ResponseEntity<List<DismissalDTO>> getAllByWorker(
            @PathVariable @Parameter(name = "worker id", example = "1") Long workerId) {
        log.trace(ApiLogMessage.NAME_OF_CURRENT_METHOD.getValue(), LogUtils.getMethodName());
        List<DismissalDTO> response = dismissalService.getAllByWorker(workerId);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    @Operation(summary = "Create a dismissal", description = "Creates a new dismissal.")
    public ResponseEntity<DismissalDTO> create(
            @RequestBody @Valid DismissalPostRequest request) {
        log.trace(ApiLogMessage.NAME_OF_CURRENT_METHOD.getValue(), LogUtils.getMethodName());
        DismissalDTO response = dismissalService.create(request);
        return ResponseEntity.created(LocationUtils.buildLocation(request.getWorkerId(),
                                                                  securityContextFacade.getCurrentUserId())).eTag(
                EtagUtils.toEtag(response.version())).body(response);
    }

    @PutMapping("/{workerId}")
    @Operation(summary = "Replace dismissal by worker id", description = "Replace an existing dismissal with new data.")
    public ResponseEntity<DismissalDTO> replace(
            @PathVariable @Parameter(name = "worker id", example = "1") Long workerId,
            @RequestBody @Valid DismissalPutRequest request,
            @Parameter(name = "If-Match",
                       in = ParameterIn.HEADER,
                       required = true,
                       description = "ETag of the resource") @RequestHeader(value = "If-Match") String ifMatch) {
        log.trace(ApiLogMessage.NAME_OF_CURRENT_METHOD.getValue(), LogUtils.getMethodName());
        long expected = EtagUtils.parseIfMatch(ifMatch);
        DismissalDTO response = dismissalService.replace(workerId, request, expected);
        return ResponseEntity.status(HttpStatus.OK)
                             .eTag(EtagUtils.toEtag(response.version()))
                             .body(response);
    }

    @PatchMapping("/{workerId}")
    @Operation(summary = "Update dismissal by worker id", description = "Update an existing dismissal with new data.")
    public ResponseEntity<DismissalDTO> update(
            @PathVariable @Parameter(name = "worker id", example = "1") Long workerId,
            @RequestBody @Valid DismissalPatchRequest request,
            @Parameter(name = "If-Match",
                       in = ParameterIn.HEADER,
                       required = true,
                       description = "ETag of the resource") @RequestHeader(value = "If-Match") String ifMatch) {
        log.trace(ApiLogMessage.NAME_OF_CURRENT_METHOD.getValue(), LogUtils.getMethodName());
        long expected = EtagUtils.parseIfMatch(ifMatch);
        DismissalDTO response = dismissalService.update(workerId, request, expected);
        return ResponseEntity.status(HttpStatus.OK)
                             .eTag(EtagUtils.toEtag(response.version()))
                             .body(response);
    }

}
