package io.github.artsobol.kurkod.controller;

import io.github.artsobol.kurkod.model.constants.ApiLogMessage;
import io.github.artsobol.kurkod.model.dto.staff.StaffDTO;
import io.github.artsobol.kurkod.model.request.staff.StaffPatchRequest;
import io.github.artsobol.kurkod.model.request.staff.StaffPostRequest;
import io.github.artsobol.kurkod.model.request.staff.StaffPutRequest;
import io.github.artsobol.kurkod.model.response.IamResponse;
import io.github.artsobol.kurkod.service.StaffService;
import io.github.artsobol.kurkod.utils.ApiUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(value = "/api/v1/staff", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@Tag(name = "Staff", description = "Staff operations")
public class StaffController {

    private final StaffService staffService;

    @Operation(summary = "Get staff by ID", description = "Returns a single staff member by their unique identifier.")
    @GetMapping("/{id}")
    public ResponseEntity<IamResponse<StaffDTO>> getStaffInfo(@Parameter(description = "Staff identifier", example = "7") @PathVariable Integer id) {
        log.trace(ApiLogMessage.NAME_OF_CURRENT_METHOD.getValue(), ApiUtils.getMethodName());
        IamResponse<StaffDTO> response = staffService.getStaffInfo(id);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "List all staff", description = "Returns all staff positions available in the system.")
    @GetMapping
    public ResponseEntity<IamResponse<List<StaffDTO>>> getAllStaffs() {
        log.trace(ApiLogMessage.NAME_OF_CURRENT_METHOD.getValue(), ApiUtils.getMethodName());
        IamResponse<List<StaffDTO>> response = staffService.getAllStaffs();
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Create a new staff position", description = "Creates a new staff position. Name must be unique.")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<IamResponse<StaffDTO>> createStaff(@Valid @RequestBody StaffPostRequest staffPostRequest) {
        log.trace(ApiLogMessage.NAME_OF_CURRENT_METHOD.getValue(), ApiUtils.getMethodName());
        IamResponse<StaffDTO> response = staffService.createStaff(staffPostRequest);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Replace a staff position", description = "Fully replaces an existing staff position by ID.")
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<IamResponse<StaffDTO>> updateFullyStaff(@Parameter(description = "Staff identifier", example = "7") @PathVariable Integer id,
                                                                  @Valid @RequestBody StaffPutRequest staffPutRequest) {
        log.trace(ApiLogMessage.NAME_OF_CURRENT_METHOD.getValue(), ApiUtils.getMethodName());
        IamResponse<StaffDTO> response = staffService.updateFullyStaff(id, staffPutRequest);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Partially update a staff position", description = "Applies a partial update to a staff position by ID.")
    @PatchMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<IamResponse<StaffDTO>> updatePartiallyStaff(@Parameter(description = "Staff identifier", example = "7") @PathVariable Integer id,
                                                                      @Valid @RequestBody StaffPatchRequest staffPatchRequest) {
        log.trace(ApiLogMessage.NAME_OF_CURRENT_METHOD.getValue(), ApiUtils.getMethodName());
        IamResponse<StaffDTO> response = staffService.updatePartiallyStaff(id, staffPatchRequest);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Delete a staff position", description = "Deletes a staff position by its unique identifier.")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStaff(@Parameter(description = "Staff identifier", example = "7") @PathVariable Integer id) {
        log.trace(ApiLogMessage.NAME_OF_CURRENT_METHOD.getValue(), ApiUtils.getMethodName());
        staffService.deleteStaff(id);
        return ResponseEntity.noContent().build();
    }
}
