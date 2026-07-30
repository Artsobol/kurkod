package io.github.artsobol.kurkod.exception.base;

import io.github.artsobol.kurkod.infrastructure.error.descriptor.ErrorDescriptor;

public final class Exceptions {
    private Exceptions() {}

    public static BaseException of(ErrorDescriptor e, Object... args) {
        return new BaseException(e.getCode(), e.getMessageKey(), args, e.getStatus()) {};
    }
}
