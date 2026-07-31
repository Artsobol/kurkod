package io.github.artsobol.kurkod.exception.http;

import io.github.artsobol.kurkod.exception.base.BaseException;
import java.util.Map;
import org.springframework.http.HttpStatus;

public class VersionConflictException extends BaseException {

  public VersionConflictException(String messageKey, Object... args) {
    super(messageKey, messageKey, HttpStatus.PRECONDITION_FAILED, Map.of(), null, args);
  }
}
