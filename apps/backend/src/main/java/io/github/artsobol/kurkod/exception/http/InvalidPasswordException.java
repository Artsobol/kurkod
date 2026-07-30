package io.github.artsobol.kurkod.exception.http;

import io.github.artsobol.kurkod.exception.base.BaseException;

import io.github.artsobol.kurkod.infrastructure.error.descriptor.ErrorDescriptor;
import org.springframework.http.HttpStatus;

/**
 * Thrown when an authentication attempt fails due to an invalid password.
 */
public class InvalidPasswordException extends BaseException{

    public InvalidPasswordException(String message) {
        super(message, HttpStatus.UNAUTHORIZED);
    }

    public InvalidPasswordException(ErrorDescriptor error, Object... args) {
        super(error.getCode(), error.getMessageKey(), args, error.getStatus());
    }
}
