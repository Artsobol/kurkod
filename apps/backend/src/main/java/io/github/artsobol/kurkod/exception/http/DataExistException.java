package io.github.artsobol.kurkod.exception.http;

import io.github.artsobol.kurkod.exception.base.BaseException;

import io.github.artsobol.kurkod.infrastructure.error.descriptor.ErrorDescriptor;
import org.springframework.http.HttpStatus;

/**
 * Thrown when attempting to create or save data that already exists in the system.
 * Example: trying to register a user with an email that is already taken.
 */
public class DataExistException extends BaseException{

    public DataExistException(String message) {
        super(message, HttpStatus.CONFLICT  );
    }

    public DataExistException(ErrorDescriptor error, Object... args) {
        super(error.getCode(), error.getMessageKey(), args, error.getStatus());
    }


}
