package io.github.artsobol.kurkod.exception.http;

import io.github.artsobol.kurkod.exception.base.BaseException;

import io.github.artsobol.kurkod.infrastructure.error.descriptor.ErrorDescriptor;
import org.springframework.http.HttpStatus;

/**
 * Thrown when a requested resource cannot be found in the system.
 * Example: user not found, breed not found, etc.
 */
public class NotFoundException extends BaseException {
    public NotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }

    public NotFoundException(ErrorDescriptor error, Object... args) {
        super(error.getCode(), error.getMessageKey(), args, error.getStatus());
    }
}
