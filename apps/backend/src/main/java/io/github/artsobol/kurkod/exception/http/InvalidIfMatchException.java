package io.github.artsobol.kurkod.exception.http;

import io.github.artsobol.kurkod.exception.base.BaseException;

import io.github.artsobol.kurkod.infrastructure.error.descriptor.ErrorDescriptor;
import org.springframework.http.HttpStatus;

public class InvalidIfMatchException extends BaseException {

    public InvalidIfMatchException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }

    public InvalidIfMatchException(ErrorDescriptor error, Object... args) {
        super(error.getCode(), error.getMessageKey(), args, error.getStatus());
    }
}
