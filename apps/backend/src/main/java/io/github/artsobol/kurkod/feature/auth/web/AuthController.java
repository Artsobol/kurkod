package io.github.artsobol.kurkod.feature.auth.web;

import io.github.artsobol.kurkod.config.security.CookieProperties;
import io.github.artsobol.kurkod.feature.auth.dto.request.LoginRequest;
import io.github.artsobol.kurkod.feature.auth.dto.request.RefreshTokenRequest;
import io.github.artsobol.kurkod.feature.auth.dto.request.RegistrationRequest;
import io.github.artsobol.kurkod.feature.auth.dto.request.SessionMetadata;
import io.github.artsobol.kurkod.feature.auth.dto.response.AuthResponse;
import io.github.artsobol.kurkod.feature.auth.refreshtoken.dto.request.RotateRefreshTokenRequest;
import io.github.artsobol.kurkod.feature.auth.service.LoginService;
import io.github.artsobol.kurkod.feature.auth.service.RefreshService;
import io.github.artsobol.kurkod.feature.auth.service.RegistrationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.WebUtils;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/auth", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Authentication", description = "Authentication operations")
public class AuthController {

  private final CookieProperties cookieProperties;
  private final RegistrationService registrationService;
  private final LoginService loginService;
  private final RefreshService refreshService;

  @Operation(summary = "Register new user")
  @PostMapping(value = "/register", consumes = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<AuthResponse> register(
      @RequestBody @Valid RegistrationRequest request, HttpServletRequest servletRequest) {
    AuthResponse response = registrationService.register(request, metadata(servletRequest));
    return withRefreshCookie(response, HttpStatus.CREATED);
  }

  @Operation(summary = "Authenticate user")
  @PostMapping(value = "/login", consumes = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<AuthResponse> login(
      @RequestBody @Valid LoginRequest request, HttpServletRequest servletRequest) {
    AuthResponse response = loginService.login(request, metadata(servletRequest));
    return withRefreshCookie(response, HttpStatus.OK);
  }

  @Operation(summary = "Refresh access token")
  @PostMapping("/refresh")
  public ResponseEntity<AuthResponse> refresh(
      @RequestBody(required = false) RefreshTokenRequest body,
      HttpServletRequest servletRequest) {
    String rawToken = resolveRefreshToken(body, servletRequest);
    AuthResponse response = refreshService.refresh(
        new RotateRefreshTokenRequest(
            rawToken,
            servletRequest.getRemoteAddr(),
            normalize(servletRequest.getHeader(HttpHeaders.USER_AGENT))));
    return withRefreshCookie(response, HttpStatus.OK);
  }

  @Operation(summary = "Log out current session")
  @PostMapping("/logout")
  public ResponseEntity<Void> logout(
      @RequestBody(required = false) RefreshTokenRequest body,
      HttpServletRequest servletRequest) {
    refreshService.logout(resolveRefreshToken(body, servletRequest));
    return ResponseEntity.noContent()
        .header(HttpHeaders.SET_COOKIE, deleteCookie().toString())
        .build();
  }

  private SessionMetadata metadata(HttpServletRequest request) {
    String userAgent = normalize(request.getHeader(HttpHeaders.USER_AGENT));
    return new SessionMetadata(request.getRemoteAddr(), userAgent, deviceName(userAgent));
  }

  private String resolveRefreshToken(RefreshTokenRequest body, HttpServletRequest request) {
    if (body != null && StringUtils.hasText(body.refreshToken())) {
      return body.refreshToken();
    }
    Cookie cookie = WebUtils.getCookie(request, cookieProperties.cookieName());
    return cookie == null ? null : cookie.getValue();
  }

  private ResponseEntity<AuthResponse> withRefreshCookie(
      AuthResponse response, HttpStatus status) {
    ResponseCookie cookie = cookie(response.refreshToken(), cookieProperties.maxAge());
    return ResponseEntity.status(status)
        .header(HttpHeaders.SET_COOKIE, cookie.toString())
        .body(response);
  }

  private ResponseCookie deleteCookie() {
    return cookie("", java.time.Duration.ZERO);
  }

  private ResponseCookie cookie(String value, java.time.Duration maxAge) {
    return ResponseCookie.from(cookieProperties.cookieName(), value)
        .httpOnly(true)
        .secure(cookieProperties.secure())
        .sameSite(cookieProperties.sameSite())
        .path(cookieProperties.path())
        .maxAge(maxAge)
        .build();
  }

  private static String normalize(String value) {
    return StringUtils.hasText(value) ? value : "unknown";
  }

  private static String deviceName(String userAgent) {
    return userAgent.length() <= 120 ? userAgent : userAgent.substring(0, 120);
  }
}
