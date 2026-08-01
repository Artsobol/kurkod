package io.github.artsobol.kurkod.infrastructure.error.advice;

import io.github.artsobol.kurkod.exception.base.BaseException;
import io.github.artsobol.kurkod.infrastructure.error.dto.ErrorResponse;
import io.github.artsobol.kurkod.infrastructure.error.dto.ValidationErrorResponse;
import io.github.artsobol.kurkod.infrastructure.error.dto.ValidationFieldError;
import io.github.artsobol.kurkod.infrastructure.localization.MessageService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import java.time.Instant;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class CommonControllerAdvice {

  private final MessageService messageService;

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ValidationErrorResponse> handleMethodArgumentNotValidException(
      MethodArgumentNotValidException ex, HttpServletRequest request) {
    List<ValidationFieldError> errors =
        ex.getBindingResult().getFieldErrors().stream()
            .map(
                err ->
                    new ValidationFieldError(
                        err.getField(), messageService.resolveValidationMessage(err)))
            .toList();

    ValidationErrorResponse response = buildValidationErrorResponse(request, errors);
    log.warn("Validation error for request URI: {}. Errors: {}", request.getRequestURI(), errors);

    return ResponseEntity.badRequest().body(response);
  }

  @ExceptionHandler(ConstraintViolationException.class)
  public ResponseEntity<ValidationErrorResponse> handleConstraintViolationException(
      ConstraintViolationException ex, HttpServletRequest request) {
    List<ValidationFieldError> errors =
        ex.getConstraintViolations().stream()
            .map(
                violation ->
                    new ValidationFieldError(extractFieldName(violation), violation.getMessage()))
            .toList();

    ValidationErrorResponse response = buildValidationErrorResponse(request, errors);
    log.warn(
        "Constraint validation error for request URI: {}. Errors: {}",
        request.getRequestURI(),
        errors);

    return ResponseEntity.badRequest().body(response);
  }

  @ExceptionHandler(HandlerMethodValidationException.class)
  public ResponseEntity<ValidationErrorResponse> handleHandlerMethodValidationException(
      HandlerMethodValidationException ex, HttpServletRequest request) {
    List<ValidationFieldError> errors =
        ex.getParameterValidationResults().stream()
            .flatMap(
                result ->
                    result.getResolvableErrors().stream()
                        .map(
                            error ->
                                new ValidationFieldError(
                                    result.getMethodParameter().getParameterName(),
                                    messageService.resolveValidationMessage(error))))
            .toList();

    return ResponseEntity.badRequest().body(buildValidationErrorResponse(request, errors));
  }

  @ExceptionHandler(MissingServletRequestParameterException.class)
  public ResponseEntity<ValidationErrorResponse> handleMissingServletRequestParameterException(
      MissingServletRequestParameterException ex, HttpServletRequest request) {
    HttpStatus status = HttpStatus.BAD_REQUEST;

    String parameterName = ex.getParameterName();
    String localizedMessage =
        messageService.createMessage("common.parameter.missing", new Object[] {parameterName});
    List<ValidationFieldError> errors =
        List.of(new ValidationFieldError(parameterName, localizedMessage));

    String message = messageService.createMessage("common.validation.failed", null);

    ValidationErrorResponse response =
        new ValidationErrorResponse(
            Instant.now(),
            status.value(),
            status.getReasonPhrase(),
            message,
            request.getRequestURI(),
            errors);

    log.warn(
        "Missing request parameter for URI: {}. Parameter: {}",
        request.getRequestURI(),
        parameterName);

    return ResponseEntity.status(status).body(response);
  }

  @ExceptionHandler(MissingRequestHeaderException.class)
  public ResponseEntity<ValidationErrorResponse> handleMissingRequestHeaderException(
      MissingRequestHeaderException ex, HttpServletRequest request) {
    String headerName = ex.getHeaderName();
    String message =
        messageService.createMessage("common.header.missing", new Object[] {headerName});
    List<ValidationFieldError> errors =
        List.of(new ValidationFieldError(headerName, message));

    return ResponseEntity.badRequest().body(buildValidationErrorResponse(request, errors));
  }

  @ExceptionHandler(MethodArgumentTypeMismatchException.class)
  public ResponseEntity<ValidationErrorResponse> handleMethodArgumentTypeMismatchException(
      MethodArgumentTypeMismatchException ex, HttpServletRequest request) {
    String parameterName = ex.getName();
    String message =
        messageService.createMessage("common.parameter.invalid", new Object[] {parameterName});
    List<ValidationFieldError> errors =
        List.of(new ValidationFieldError(parameterName, message));

    return ResponseEntity.badRequest().body(buildValidationErrorResponse(request, errors));
  }

  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ResponseEntity<ErrorResponse> handleHttpMessageNotReadableException(
      HttpMessageNotReadableException ex, HttpServletRequest request) {
    return buildErrorResponse(
        request, HttpStatus.BAD_REQUEST, "BAD_REQUEST", "common.json.malformed");
  }

  @ExceptionHandler(NoResourceFoundException.class)
  public ResponseEntity<ErrorResponse> handleNoResourceFoundException(
      NoResourceFoundException ex, HttpServletRequest request) {
    return buildErrorResponse(
        request, HttpStatus.NOT_FOUND, "NOT_FOUND", "common.resource.not.found");
  }

  @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
  public ResponseEntity<ErrorResponse> handleHttpRequestMethodNotSupportedException(
      HttpRequestMethodNotSupportedException ex, HttpServletRequest request) {
    return buildErrorResponse(
        request, HttpStatus.METHOD_NOT_ALLOWED, "METHOD_NOT_ALLOWED", "common.method.not.allowed");
  }

  @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
  public ResponseEntity<ErrorResponse> handleHttpMediaTypeNotSupportedException(
      HttpMediaTypeNotSupportedException ex, HttpServletRequest request) {
    return buildErrorResponse(
        request,
        HttpStatus.UNSUPPORTED_MEDIA_TYPE,
        "UNSUPPORTED_MEDIA_TYPE",
        "common.media.type.unsupported");
  }

  @ExceptionHandler(HttpMediaTypeNotAcceptableException.class)
  public ResponseEntity<ErrorResponse> handleHttpMediaTypeNotAcceptableException(
      HttpMediaTypeNotAcceptableException ex, HttpServletRequest request) {
    return buildErrorResponse(
        request, HttpStatus.NOT_ACCEPTABLE, "NOT_ACCEPTABLE", "common.not.acceptable");
  }

  @ExceptionHandler(BaseException.class)
  public ResponseEntity<ErrorResponse> handleBaseException(
      BaseException ex, HttpServletRequest request) {
    HttpStatus status = ex.getStatus();
    String message = messageService.createMessage(ex.getMessageKey(), ex.getMessageArgs());

    ErrorResponse response =
        ErrorResponse.create(status, ex.getErrorCode(), message, request.getRequestURI());
    log.warn(
        "Request failed: method={}, URI={}, status={}, error code={}",
        request.getMethod(),
        request.getRequestURI(),
        status.value(),
        ex.getErrorCode());

    return ResponseEntity.status(status).body(response);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse> handleException(Exception ex, HttpServletRequest request) {
    HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
    String message = messageService.createMessage("common.internal.error", null);

    ErrorResponse response =
        ErrorResponse.create(status, "INTERNAL_SERVER_ERROR", message, request.getRequestURI());
    log.error(
        "Unexpected error: method={}, URI={}, status={}, errorCode={}",
        request.getMethod(),
        request.getRequestURI(),
        500,
        "INTERNAL_SERVER_ERROR",
        ex);

    return ResponseEntity.status(status).body(response);
  }

  private ValidationErrorResponse buildValidationErrorResponse(
      HttpServletRequest request, List<ValidationFieldError> errors) {
    HttpStatus status = HttpStatus.BAD_REQUEST;
    String message = messageService.createMessage("common.validation.failed", null);

    return new ValidationErrorResponse(
        Instant.now(),
        status.value(),
        status.getReasonPhrase(),
        message,
        request.getRequestURI(),
        errors);
  }

  private ResponseEntity<ErrorResponse> buildErrorResponse(
      HttpServletRequest request, HttpStatus status, String errorCode, String messageKey) {
    String message = messageService.createMessage(messageKey, null);
    ErrorResponse response =
        ErrorResponse.create(status, errorCode, message, request.getRequestURI());
    return ResponseEntity.status(status).body(response);
  }

  private String extractFieldName(ConstraintViolation<?> violation) {
    String propertyPath = violation.getPropertyPath().toString();
    int separatorIndex = propertyPath.lastIndexOf('.');

    if (separatorIndex >= 0 && separatorIndex < propertyPath.length() - 1) {
      return propertyPath.substring(separatorIndex + 1);
    }

    return propertyPath;
  }
}
