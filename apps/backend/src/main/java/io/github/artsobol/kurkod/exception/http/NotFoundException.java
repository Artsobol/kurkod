package io.github.artsobol.kurkod.exception.http;

import io.github.artsobol.kurkod.exception.base.BaseException;
import java.util.Map;
import org.springframework.http.HttpStatus;

public class NotFoundException extends BaseException {

  public NotFoundException(String messageKey, Object... args) {
    super(messageKey, messageKey, HttpStatus.NOT_FOUND, Map.of(), null, args);
  }
}
