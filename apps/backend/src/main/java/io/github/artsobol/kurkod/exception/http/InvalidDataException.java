package io.github.artsobol.kurkod.exception.http;

import io.github.artsobol.kurkod.exception.base.BaseException;

import io.github.artsobol.kurkod.infrastructure.error.descriptor.ErrorDescriptor;

public class InvalidDataException extends BaseException{

    public InvalidDataException(ErrorDescriptor error, Object... args) {
        super(error.getCode(), error.getMessageKey(), args, error.getStatus());
    }
}
