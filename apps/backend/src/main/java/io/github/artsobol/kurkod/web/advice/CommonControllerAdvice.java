package io.github.artsobol.kurkod.web.advice;

import io.github.artsobol.kurkod.common.constants.CommonConstants;
import io.github.artsobol.kurkod.common.exception.*;
import io.github.artsobol.kurkod.common.logging.constants.AnsiColor;
import io.github.artsobol.kurkod.web.domain.common.error.CommonError;
import io.github.artsobol.kurkod.web.domain.iam.user.error.UserError;
import io.github.artsobol.kurkod.web.response.IamError;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
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

import java.util.Arrays;
import java.util.Objects;

@Slf4j
@RequiredArgsConstructor
@RestControllerAdvice
public class CommonControllerAdvice {

    private final MessageSource messageSource;

    @ExceptionHandler(BaseException.class)
    public ResponseEntity<IamError> handleBaseException(BaseException ex, HttpServletRequest request) {
        logStackTrace(ex);
        IamError error = createError(ex, request);
        return ResponseEntity.status(ex.getStatus()).contentType(MediaType.APPLICATION_JSON).body(error);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<IamError> handleMissingParameter(MissingServletRequestParameterException ex, HttpServletRequest request) {
        logStackTrace(ex);
        return handleBaseException(Exceptions.of(CommonError.BAD_REQUEST), request);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<IamError> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpServletRequest req) {
        logStackTrace(ex);
        return handleBaseException(Exceptions.of(CommonError.VALIDATION_FAILED), req);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<IamError> handleCve(ConstraintViolationException ex, HttpServletRequest req) {
        logStackTrace(ex);
        return handleBaseException(Exceptions.of(CommonError.VALIDATION_FAILED), req);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<IamError> handleUnreadable(HttpMessageNotReadableException ex, HttpServletRequest req) {
        logStackTrace(ex);
        return handleBaseException(Exceptions.of(CommonError.MALFORMED_JSON), req);
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<IamError> handle415(HttpMediaTypeNotSupportedException ex, HttpServletRequest req) {
        logStackTrace(ex);
        return handleBaseException(Exceptions.of(CommonError.UNSUPPORTED_MEDIA_TYPE), req);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<IamError> handle405(HttpRequestMethodNotSupportedException ex, HttpServletRequest req) {
        logStackTrace(ex);
        return handleBaseException(Exceptions.of(CommonError.METHOD_NOT_ALLOWED), req);
    }

    @ExceptionHandler(MissingRequestHeaderException.class)
    public ResponseEntity<IamError> handleMissingHeader(MissingRequestHeaderException ex, HttpServletRequest req) {
        if ("If-Match".equalsIgnoreCase(ex.getHeaderName())) {
            return handleBaseException(Exceptions.of(CommonError.MISSING_IF_MATCH), req);
        }
        return handleBaseException(Exceptions.of(CommonError.VALIDATION_FAILED), req);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<IamError> handleAccessDenied(AccessDeniedException ex, HttpServletRequest req) {
        logStackTrace(ex);
        return handleBaseException(Exceptions.of(UserError.HAVE_NO_ACCESS), req);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<IamError> handleAny(Exception ex, HttpServletRequest req) {
        logStackTrace(ex);
        return handleBaseException(Exceptions.of(CommonError.INTERNAL_ERROR), req);
    }

    private void logStackTrace(Exception ex) {
        StringBuilder stackTrace = new StringBuilder();

        stackTrace.append(AnsiColor.ANSI_RED);

        stackTrace.append(ex.getMessage()).append(CommonConstants.BREAK_LINE);

        if (Objects.nonNull(ex.getCause())) {
            stackTrace.append(ex.getCause().getMessage()).append(CommonConstants.BREAK_LINE);
        }

        Arrays.stream(ex.getStackTrace())
                .filter(st -> st.getClassName().startsWith(CommonConstants.TIME_ZONE_PACKAGE_NAME))
                .forEach(st -> stackTrace
                        .append(st.getClassName())
                        .append(".")
                        .append(st.getMethodName())
                        .append(" (")
                        .append(st.getLineNumber())
                        .append(") ")
                );

        log.error(stackTrace.append(AnsiColor.ANSI_WHITE).toString());
    }

    protected IamError createError(BaseException ex, HttpServletRequest request) {
        String message = getLocalizedMessage(ex);
        String path = request.getRequestURI();
        return IamError.createError(ex.getStatus(), ex.getCode(), message, path);
    }

    protected String getLocalizedMessage(BaseException ex) {
        return messageSource.getMessage(ex.getMessageKey(), ex.getArgs(), LocaleContextHolder.getLocale());
    }
}
