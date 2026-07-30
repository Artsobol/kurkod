package io.github.artsobol.kurkod.exception.http;

import io.github.artsobol.kurkod.exception.base.BaseException;
import java.util.Map;
import org.springframework.http.HttpStatus;

public class MissingIfMatchException extends BaseException {

    public MissingIfMatchException(String messageKey, Object... args) {
        super(messageKey, messageKey, HttpStatus.PRECONDITION_REQUIRED, Map.of(), null, args);
    }
}
