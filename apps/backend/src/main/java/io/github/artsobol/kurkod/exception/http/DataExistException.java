package io.github.artsobol.kurkod.exception.http;

import io.github.artsobol.kurkod.exception.base.BaseException;
import java.util.Map;
import org.springframework.http.HttpStatus;

public class DataExistException extends BaseException {

  public DataExistException(String messageKey, Object... args) {
    super(messageKey, messageKey, HttpStatus.CONFLICT, Map.of(), null, args);
  }
}
