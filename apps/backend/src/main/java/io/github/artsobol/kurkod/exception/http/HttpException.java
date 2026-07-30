package io.github.artsobol.kurkod.exception.http;

import io.github.artsobol.kurkod.exception.base.BaseException;
import java.util.Map;
import org.springframework.http.HttpStatus;

public class HttpException extends BaseException {

    public HttpException(String messageKey, HttpStatus status, Object... args) {
        super(messageKey, messageKey, status, Map.of(), null, args);
    }
}
