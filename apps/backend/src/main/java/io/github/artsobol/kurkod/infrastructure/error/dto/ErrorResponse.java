package io.github.artsobol.kurkod.infrastructure.error.dto;

import java.time.Instant;
import org.springframework.http.HttpStatus;

public record ErrorResponse(
    Instant timestamp, int status, String error, String errorCode, String message, String path) {

  public static ErrorResponse create(
      HttpStatus status, String errorCode, String message, String path) {
    return new ErrorResponse(
        Instant.now(), status.value(), status.getReasonPhrase(), errorCode, message, path);
  }
}
