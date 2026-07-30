package io.github.artsobol.kurkod.exception.http;

import io.github.artsobol.kurkod.exception.base.BaseException;

import io.github.artsobol.kurkod.infrastructure.error.descriptor.ErrorDescriptor;
import org.springframework.http.HttpStatus;

public class VersionConflictException extends BaseException {

    public VersionConflictException(String message) {
        super(message, HttpStatus.CONFLICT);
    }

    public VersionConflictException(ErrorDescriptor error, Object... args) {
        super(error.getCode(), error.getMessageKey(), args, error.getStatus());
    }
}
