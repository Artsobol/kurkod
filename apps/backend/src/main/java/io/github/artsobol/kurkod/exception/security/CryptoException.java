package io.github.artsobol.kurkod.exception.security;

import io.github.artsobol.kurkod.exception.base.BaseException;
import java.util.Map;
import org.springframework.http.HttpStatus;

public class CryptoException extends BaseException {
  public CryptoException(String messageKey, Object... messageArgs) {
    super(messageKey, messageKey, HttpStatus.INTERNAL_SERVER_ERROR, Map.of(), null, messageArgs);
  }
}
