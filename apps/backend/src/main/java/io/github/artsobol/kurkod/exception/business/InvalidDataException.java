package io.github.artsobol.kurkod.exception.business;

import io.github.artsobol.kurkod.exception.base.BaseException;
import java.util.Map;
import org.springframework.http.HttpStatus;

public class InvalidDataException extends BaseException {

  public InvalidDataException(String messageKey, Object... args) {
    super(messageKey, messageKey, HttpStatus.BAD_REQUEST, Map.of(), null, args);
  }
}
