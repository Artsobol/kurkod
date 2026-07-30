package io.github.artsobol.kurkod.feature.iam.web;

import io.github.artsobol.kurkod.infrastructure.util.EtagUtils;
import io.github.artsobol.kurkod.feature.iam.dto.request.ChangeRoleRequest;
import io.github.artsobol.kurkod.feature.iam.service.AdminUserService;
import io.github.artsobol.kurkod.feature.iam.dto.response.UserDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/admin/users/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Admin Users", description = "Administrative user management operations")
public class AdminUserController {

    private final AdminUserService userService;

    @Operation(summary = "Change user role")
    @PatchMapping("/role")
    public ResponseEntity<UserDTO> changeRole(
            @PathVariable(name = "id") Long userId,

            @Valid @RequestBody ChangeRoleRequest request,

            @RequestHeader(HttpHeaders.IF_MATCH) String ifMatch) {
        long expected = EtagUtils.parseIfMatch(ifMatch);
        UserDTO response = userService.changeUserRole(userId, request, expected);

        return ResponseEntity.ok()
                             .eTag(EtagUtils.toEtag(response.version()))
                             .body(response);
    }

    @Operation(summary = "Activate user")
    @PostMapping("/activate")
    public ResponseEntity<UserDTO> activateUser(
            @PathVariable(name = "id") Long userId,

            @RequestHeader(HttpHeaders.IF_MATCH) String ifMatch) {
        long expected = EtagUtils.parseIfMatch(ifMatch);
        UserDTO response = userService.activateUser(userId, expected);

        return ResponseEntity.ok()
                             .eTag(EtagUtils.toEtag(response.version()))
                             .body(response);
    }

    @Operation(summary = "Deactivate user")
    @PostMapping("/deactivate")
    public ResponseEntity<UserDTO> deactivateUser(
            @PathVariable(name = "id") Long userId,

            @RequestHeader(HttpHeaders.IF_MATCH) String ifMatch) {
        long expected = EtagUtils.parseIfMatch(ifMatch);
        UserDTO response = userService.deactivateUser(userId, expected);

        return ResponseEntity.ok()
                             .eTag(EtagUtils.toEtag(response.version()))
                             .body(response);
    }
}
