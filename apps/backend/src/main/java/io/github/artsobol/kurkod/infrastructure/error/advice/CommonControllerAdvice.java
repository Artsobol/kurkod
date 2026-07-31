package io.github.artsobol.kurkod.infrastructure.error.advice;

import io.github.artsobol.kurkod.exception.base.BaseException;
import io.github.artsobol.kurkod.exception.http.HttpException;
import io.github.artsobol.kurkod.exception.http.MissingIfMatchException;
import io.github.artsobol.kurkod.infrastructure.error.dto.IamError;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import java.util.Locale;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestControllerAdvice
public class CommonControllerAdvice {

    private final MessageSource messageSource;

    @ExceptionHandler(BaseException.class)
    public ResponseEntity<IamError> handleBaseException(BaseException ex, HttpServletRequest request) {
        return buildResponse(ex, request);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<IamError> handleMissingParameter(
            HttpServletRequest request) {
        return buildResponse(new HttpException("common.bad.request", HttpStatus.BAD_REQUEST), request);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<IamError> handleMethodArgumentNotValid(
            HttpServletRequest request) {
        return buildResponse(new HttpException("common.validation.failed", HttpStatus.BAD_REQUEST), request);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<IamError> handleConstraintViolation(
            HttpServletRequest request) {
        return buildResponse(new HttpException("common.validation.failed", HttpStatus.BAD_REQUEST), request);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<IamError> handleUnreadableBody(
            HttpServletRequest request) {
        return buildResponse(new HttpException("common.json.malformed", HttpStatus.BAD_REQUEST), request);
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<IamError> handleUnsupportedMediaType(
            HttpServletRequest request) {
        return buildResponse(
                new HttpException("common.media.type.unsupported", HttpStatus.UNSUPPORTED_MEDIA_TYPE),
                request);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<IamError> handleMethodNotAllowed(
            HttpServletRequest request) {
        return buildResponse(
                new HttpException("common.method.not.allowed", HttpStatus.METHOD_NOT_ALLOWED),
                request);
    }

    @ExceptionHandler(MissingRequestHeaderException.class)
    public ResponseEntity<IamError> handleMissingHeader(MissingRequestHeaderException ex, HttpServletRequest request) {
        if ("If-Match".equalsIgnoreCase(ex.getHeaderName())) {
            return buildResponse(new MissingIfMatchException("common.if.match.missing"), request);
        }
        return buildResponse(new HttpException("common.validation.failed", HttpStatus.BAD_REQUEST), request);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<IamError> handleAccessDenied(
            HttpServletRequest request) {
        return buildResponse(new HttpException("user.access.denied", HttpStatus.FORBIDDEN), request);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<IamError> handleUnexpected(Exception ex, HttpServletRequest request) {
        return buildResponse(
                new HttpException("common.internal.error", HttpStatus.INTERNAL_SERVER_ERROR),
                request);
    }

    private ResponseEntity<IamError> buildResponse(BaseException ex, HttpServletRequest request) {
        IamError error = createError(ex, request);
        return ResponseEntity.status(error.getStatus()).contentType(MediaType.APPLICATION_JSON).body(error);
    }

    protected IamError createError(BaseException ex, HttpServletRequest request) {
        String message = getLocalizedMessage(ex);
        String path = request.getRequestURI();
        return IamError.createError(ex.getStatus(), ex.getErrorCode(), message, path);
    }

    protected String getLocalizedMessage(BaseException ex) {
        Locale locale = LocaleContextHolder.getLocale();
        try {
            return messageSource.getMessage(ex.getMessageKey(), ex.getMessageArgs(), locale);
        } catch (NoSuchMessageException e) {
            if (ex.getMessage() != null) {
                return ex.getMessage();
            }
            return ex.getMessageKey();
        }
    }
}
