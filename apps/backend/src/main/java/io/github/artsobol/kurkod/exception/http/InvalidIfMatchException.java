package io.github.artsobol.kurkod.exception.http;

import io.github.artsobol.kurkod.exception.base.BaseException;
import java.util.Map;
import org.springframework.http.HttpStatus;

public class InvalidIfMatchException extends BaseException {

  public InvalidIfMatchException(String messageKey, Object... args) {
    super(messageKey, messageKey, HttpStatus.BAD_REQUEST, Map.of(), null, args);
  }
}
