package io.github.artsobol.kurkod.common.exception;

import org.springframework.http.HttpStatus;

/**
 * Thrown when a requested resource cannot be found in the system.
 * Example: user not found, breed not found, etc.
 */
public class NotFoundException extends BaseException {
    public NotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }
}
