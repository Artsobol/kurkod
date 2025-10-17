package io.github.artsobol.kurkod.controller;

import io.github.artsobol.kurkod.model.constants.ApiLogMessage;
import io.github.artsobol.kurkod.model.dto.user.UserDTO;
import io.github.artsobol.kurkod.model.request.user.UserPatchRequest;
import io.github.artsobol.kurkod.model.request.user.UserPostRequest;
import io.github.artsobol.kurkod.model.request.user.UserPutRequest;
import io.github.artsobol.kurkod.model.response.IamResponse;
import io.github.artsobol.kurkod.service.UserService;
import io.github.artsobol.kurkod.utils.ApiUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@Validated
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/users", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Users", description = "User operations")
public class UserController {

    private final UserService userService;

    @Operation(summary = "Get user by ID", description = "Returns user information by unique identifier.")
    @GetMapping("/id/{userId}")
    public ResponseEntity<IamResponse<UserDTO>> getById(@Parameter(
            description = "User identifier",
            example = "5"
    ) @PathVariable(name = "userId") Integer userId) {
        log.trace(ApiLogMessage.NAME_OF_CURRENT_METHOD.getValue(), ApiUtils.getMethodName());
        UserDTO response = userService.getById(userId);
        return ResponseEntity.ok(IamResponse.createSuccessful(response));
    }

    @Operation(summary = "Get user by username", description = "Returns user information by username.")
    @GetMapping("/username/{username}")
    public ResponseEntity<IamResponse<UserDTO>> getByUsername(@Parameter(
            description = "Username of the user",
            example = "John"
    ) String username) {
        log.trace(ApiLogMessage.NAME_OF_CURRENT_METHOD.getValue(), ApiUtils.getMethodName());
        UserDTO response = userService.getByUsername(username);
        return ResponseEntity.ok(IamResponse.createSuccessful(response));
    }

    @Operation(summary = "Create a new user", description = "Creates a new user account.")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<IamResponse<UserDTO>> create(@RequestBody @Valid UserPostRequest userPostRequest) {
        log.trace(ApiLogMessage.NAME_OF_CURRENT_METHOD.getValue(), ApiUtils.getMethodName());
        UserDTO response = userService.create(userPostRequest);
        return ResponseEntity.ok(IamResponse.createSuccessful(response));
    }

    @Operation(summary = "Partially update user", description = "Applies a partial update to the user account.")
    @PatchMapping(value = "/{userId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<IamResponse<UserDTO>> updatePartiallyUser(@Parameter(
            description = "User identifier",
            example = "5"
    ) @PathVariable(name = "userId") Integer userId, @RequestBody @Valid UserPatchRequest userPatchRequest) {
        log.trace(ApiLogMessage.NAME_OF_CURRENT_METHOD.getValue(), ApiUtils.getMethodName());
        UserDTO response = userService.update(userId, userPatchRequest);
        return ResponseEntity.ok(IamResponse.createSuccessful(response));
    }

    @Operation(summary = "Fully update user", description = "Fully replaces user data by ID.")
    @PutMapping(value = "/{userId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<IamResponse<UserDTO>> updateFullyUser(@Parameter(
            description = "User identifier",
            example = "5"
    ) @PathVariable(name = "userId") Integer userId, @RequestBody @Valid UserPutRequest userPutRequest) {
        log.trace(ApiLogMessage.NAME_OF_CURRENT_METHOD.getValue(), ApiUtils.getMethodName());
        UserDTO response = userService.replace(userId, userPutRequest);
        return ResponseEntity.ok(IamResponse.createSuccessful(response));
    }

    @Operation(summary = "Delete user", description = "Deletes the user account by ID.")
    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteById(@Parameter(
            description = "User identifier",
            example = "5"
    ) @PathVariable(name = "userId") Integer userId) {
        log.trace(ApiLogMessage.NAME_OF_CURRENT_METHOD.getValue(), ApiUtils.getMethodName());
        userService.deleteById(userId);
        return ResponseEntity.noContent().build();
    }
}
