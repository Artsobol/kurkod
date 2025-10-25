package io.github.artsobol.kurkod.web.dismissal;

import io.github.artsobol.kurkod.common.constants.ApiLogMessage;
import io.github.artsobol.kurkod.common.util.LogUtils;
import io.github.artsobol.kurkod.web.domain.dismissal.model.dto.DismissalDTO;
import io.github.artsobol.kurkod.web.domain.dismissal.model.request.DismissalPatchRequest;
import io.github.artsobol.kurkod.web.domain.dismissal.model.request.DismissalPostRequest;
import io.github.artsobol.kurkod.web.domain.dismissal.model.request.DismissalPutRequest;
import io.github.artsobol.kurkod.web.domain.dismissal.service.api.DismissalService;
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
@RequestMapping("/api/v1/dismissals")
@RequiredArgsConstructor
@Tag(name = "Dismissals", description = "Dismissals API")
public class DismissalController {

    private final DismissalService dismissalService;

    @GetMapping("/workers/{workerId}/dismissed/{dismissedId}")
    @Operation(summary = "Get dismissal by worker and dismissed", description = "Returns a single dismissal by worker and dismissed.")
    public ResponseEntity<IamResponse<DismissalDTO>> getByWorkerAndDismissed(
            @Parameter(name = "worker id", example = "1") @PathVariable(name = "workerId") Integer workerId,
            @Parameter(name = "dismissed id", example = "1") @PathVariable(name = "dismissedId") Integer dismissedId
    ) {
        log.trace(ApiLogMessage.NAME_OF_CURRENT_METHOD.getValue(), LogUtils.getMethodName());
        DismissalDTO response = dismissalService.getByWorkerAndDismissed(workerId, dismissedId);
        return ResponseEntity.ok(IamResponse.createSuccessful(response));
    }

    @GetMapping("/dismissed/{dismissedId}")
    @Operation(summary = "Get dismissals by dismissed", description = "Returns all dismissals by dismissed.")
    public ResponseEntity<IamResponse<List<DismissalDTO>>> getAllByDismissed(
            @Parameter(name = "dismissed id", example = "1") @PathVariable(name = "dismissedId") Integer dismissedId
    ) {
        log.trace(ApiLogMessage.NAME_OF_CURRENT_METHOD.getValue(), LogUtils.getMethodName());
        List<DismissalDTO> response = dismissalService.getAllByDismissed(dismissedId);
        return ResponseEntity.ok(IamResponse.createSuccessful(response));
    }

    @GetMapping("/workers/{workerId}")
    @Operation(summary = "Get dismissals by worker", description = "Returns all dismissals by worker.")
    public ResponseEntity<IamResponse<List<DismissalDTO>>> getAllByWorker(
            @Parameter(name = "worker id", example = "1") @PathVariable(name = "workerId") Integer workerId
    ) {
        log.trace(ApiLogMessage.NAME_OF_CURRENT_METHOD.getValue(), LogUtils.getMethodName());
        List<DismissalDTO> response = dismissalService.getAllByWorker(workerId);
        return ResponseEntity.ok(IamResponse.createSuccessful(response));
    }

    @PostMapping
    @Operation(summary = "Create a dismissal", description = "Creates a new dismissal.")
    public ResponseEntity<IamResponse<DismissalDTO>> create(
            @RequestBody @Valid DismissalPostRequest dismissalPostRequest
    ) {
        log.trace(ApiLogMessage.NAME_OF_CURRENT_METHOD.getValue(), LogUtils.getMethodName());
        DismissalDTO response = dismissalService.create(dismissalPostRequest);
        return ResponseEntity.ok(IamResponse.createSuccessful(response));
    }

    @PutMapping("/{workerId}")
    @Operation(summary = "Replace dismissal by worker id", description = "Replace an existing dismissal with new data.")
    public ResponseEntity<IamResponse<DismissalDTO>> replace(
            @Parameter(name = "worker id", example = "1") @PathVariable(name = "workerId") Integer workerId,
            @RequestBody @Valid DismissalPutRequest dismissalPutRequest
            ) {
        log.trace(ApiLogMessage.NAME_OF_CURRENT_METHOD.getValue(), LogUtils.getMethodName());
        DismissalDTO response = dismissalService.replace(workerId, dismissalPutRequest);
        return ResponseEntity.ok(IamResponse.createSuccessful(response));
    }

    @PatchMapping("/{workerId}")
    @Operation(summary = "Update dismissal by worker id", description = "Update an existing dismissal with new data.")
    public ResponseEntity<IamResponse<DismissalDTO>> update(
            @Parameter(name = "worker id", example = "1") @PathVariable(name = "workerId") Integer workerId,
            @RequestBody @Valid DismissalPatchRequest dismissalPatchRequest
            ) {
        log.trace(ApiLogMessage.NAME_OF_CURRENT_METHOD.getValue(), LogUtils.getMethodName());
        DismissalDTO response = dismissalService.update(workerId, dismissalPatchRequest);
        return ResponseEntity.ok(IamResponse.createSuccessful(response));
    }

}
