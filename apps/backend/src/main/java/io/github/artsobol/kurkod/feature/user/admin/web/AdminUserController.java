package io.github.artsobol.kurkod.feature.user.admin.web;

import io.github.artsobol.kurkod.feature.user.admin.dto.request.ChangeRoleRequest;
import io.github.artsobol.kurkod.feature.user.dto.response.UserResponse;
import io.github.artsobol.kurkod.feature.user.admin.service.AdminUserService;
import io.github.artsobol.kurkod.infrastructure.utils.EtagUtils;
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
@RequestMapping(value = "/admin/users/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Admin Users", description = "Administrative user management operations")
public class AdminUserController {

    private final AdminUserService userService;

    @Operation(summary = "Change user role")
    @PatchMapping("/role")
    public ResponseEntity<UserResponse> changeRole(
            @PathVariable(name = "id") Long userId,

            @Valid @RequestBody ChangeRoleRequest request,

            @RequestHeader(value = HttpHeaders.IF_MATCH, required = false) String ifMatch) {
        long expected = EtagUtils.parseIfMatch(ifMatch);
        UserResponse response = userService.changeUserRole(userId, request, expected);

        return ResponseEntity.ok()
                             .eTag(EtagUtils.toEtag(response.version()))
                             .body(response);
    }

    @Operation(summary = "Activate user")
    @PostMapping("/activate")
    public ResponseEntity<UserResponse> activateUser(
            @PathVariable(name = "id") Long userId,

            @RequestHeader(value = HttpHeaders.IF_MATCH, required = false) String ifMatch) {
        long expected = EtagUtils.parseIfMatch(ifMatch);
        UserResponse response = userService.activateUser(userId, expected);

        return ResponseEntity.ok()
                             .eTag(EtagUtils.toEtag(response.version()))
                             .body(response);
    }

    @Operation(summary = "Deactivate user")
    @PostMapping("/deactivate")
    public ResponseEntity<UserResponse> deactivateUser(
            @PathVariable(name = "id") Long userId,

            @RequestHeader(value = HttpHeaders.IF_MATCH, required = false) String ifMatch) {
        long expected = EtagUtils.parseIfMatch(ifMatch);
        UserResponse response = userService.deactivateUser(userId, expected);

        return ResponseEntity.ok()
                             .eTag(EtagUtils.toEtag(response.version()))
                             .body(response);
    }
}
