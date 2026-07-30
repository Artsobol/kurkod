package io.github.artsobol.kurkod.exception.http;

import io.github.artsobol.kurkod.exception.base.BaseException;

import io.github.artsobol.kurkod.infrastructure.error.descriptor.ErrorDescriptor;
import org.springframework.http.HttpStatus;

public class MatchFailedException extends BaseException {

    public MatchFailedException(String message) {
        super(message, HttpStatus.PRECONDITION_FAILED);
    }

    public MatchFailedException(ErrorDescriptor error, Object... args) {
        super(error.getCode(), error.getMessageKey(), args, error.getStatus());
    }
}
