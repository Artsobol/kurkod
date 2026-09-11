package io.github.artsobol.kurkod.exception.security;

import io.github.artsobol.kurkod.exception.base.BaseException;
import java.util.Map;
import org.springframework.http.HttpStatus;

public class AuthenticationException extends BaseException {
  public AuthenticationException(String messageKey, Object... messageArgs) {
    super(messageKey, messageKey, HttpStatus.UNAUTHORIZED, Map.of(), null, messageArgs);
  }
}
