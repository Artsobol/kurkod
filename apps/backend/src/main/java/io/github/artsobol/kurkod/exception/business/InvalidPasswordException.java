package io.github.artsobol.kurkod.exception.business;

import io.github.artsobol.kurkod.exception.base.BaseException;
import java.util.Map;
import org.springframework.http.HttpStatus;

public class InvalidPasswordException extends BaseException {

  public InvalidPasswordException(String messageKey, Object... args) {
    super(messageKey, messageKey, HttpStatus.UNAUTHORIZED, Map.of(), null, args);
  }
}
