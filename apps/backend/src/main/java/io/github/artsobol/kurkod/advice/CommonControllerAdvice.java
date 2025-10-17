package io.github.artsobol.kurkod.advice;

import io.github.artsobol.kurkod.model.constants.ApiConstants;
import io.github.artsobol.kurkod.model.exception.InvalidPasswordException;
import io.github.artsobol.kurkod.model.exception.NotFoundException;
import io.github.artsobol.kurkod.model.response.IamError;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.AccessDeniedException;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Slf4j
@RestControllerAdvice
public class CommonControllerAdvice {

    @ExceptionHandler(InvalidPasswordException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    protected IamError handleInvalidPasswordException(InvalidPasswordException exception, HttpServletRequest request) {
        logStackTrace(exception);

        return IamError.createError(exception.getStatus(), exception.getMessage(), request.getRequestURI());
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    protected IamError handleAccessDeniedException(Exception e, HttpServletRequest request) {
        logStackTrace(e);
        return IamError.createError(HttpStatus.FORBIDDEN, e.getMessage(), request.getRequestURI());
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    protected IamError handleNotFoundException(NotFoundException exception, HttpServletRequest request) {
        logStackTrace(exception);

        return IamError.createError(exception.getStatus(), exception.getMessage(), request.getRequestURI());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected IamError handleMethodArgumentNotValidException(MethodArgumentNotValidException exception, HttpServletRequest request) {
        logStackTrace(exception);

        List<String> errors = exception.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(err -> "%s: %s".formatted(err.getField(), err.getDefaultMessage()))
                .toList();

        return IamError.validationError(errors, request.getRequestURI());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    protected IamError handleException(Exception e, HttpServletRequest request) {
        logStackTrace(e);

        return IamError.createError(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error", request.getRequestURI());
    }

    private void logStackTrace(Exception ex) {
        StringBuilder stackTrace = new StringBuilder();

        stackTrace.append(ApiConstants.ANSI_RED);

        stackTrace.append(ex.getMessage()).append(ApiConstants.BREAK_LINE);

        if (Objects.nonNull(ex.getCause())) {
            stackTrace.append(ex.getCause().getMessage()).append(ApiConstants.BREAK_LINE);
        }

        Arrays.stream(ex.getStackTrace())
                .filter(st -> st.getClassName().startsWith(ApiConstants.TIME_ZONE_PACKAGE_NAME))
                .forEach(st -> stackTrace
                        .append(st.getClassName())
                        .append(".")
                        .append(st.getMethodName())
                        .append(" (")
                        .append(st.getLineNumber())
                        .append(") ")
                );

        log.error(stackTrace.append(ApiConstants.ANSI_WHITE).toString());
    }
}
