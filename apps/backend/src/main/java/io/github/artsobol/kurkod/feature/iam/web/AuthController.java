package io.github.artsobol.kurkod.feature.iam.web;

import io.github.artsobol.kurkod.feature.iam.dto.request.LoginRequest;
import io.github.artsobol.kurkod.feature.iam.dto.request.RegistrationRequest;
import io.github.artsobol.kurkod.feature.iam.dto.response.UserProfileResponse;
import io.github.artsobol.kurkod.feature.iam.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/auth", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Authentication", description = "Authentication operations")
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "Authenticate user")
    @PostMapping(value = "/login", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserProfileResponse> login(@RequestBody @Valid LoginRequest loginRequest) {
        UserProfileResponse result = authService.login(loginRequest);
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "Refresh access token")
    @GetMapping("/refresh/token")
    public ResponseEntity<UserProfileResponse> refreshToken(
            @RequestParam(name = "token") String refreshToken) {

        UserProfileResponse result = authService.refreshAccessToken(refreshToken);
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "Register new user")
    @PostMapping(value = "/register", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserProfileResponse> register(
            @RequestBody @Valid RegistrationRequest registrationRequest) {
        UserProfileResponse result = authService.registerUser(registrationRequest);
        return ResponseEntity.ok(result);
    }
}
