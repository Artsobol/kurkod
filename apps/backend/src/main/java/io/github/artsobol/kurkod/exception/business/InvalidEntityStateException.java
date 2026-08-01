package io.github.artsobol.kurkod.exception.business;

import io.github.artsobol.kurkod.exception.base.BaseException;
import java.util.Map;
import org.springframework.http.HttpStatus;

public class InvalidEntityStateException extends BaseException {

  public InvalidEntityStateException(String messageKey, Object... args) {
    super(messageKey, messageKey, HttpStatus.CONFLICT, Map.of(), null, args);
  }
}
