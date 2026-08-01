package io.github.artsobol.kurkod.infrastructure.localization;

import java.util.Locale;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MessageService {

  private final MessageSource messageSource;

  public String createMessage(String key, Object[] args) {
    Locale locale = LocaleContextHolder.getLocale();
    return messageSource.getMessage(key, args, key, locale);
  }

  public String resolveValidationMessage(MessageSourceResolvable error) {
    String defaultMessage = error.getDefaultMessage();
    if (defaultMessage == null || defaultMessage.isBlank()) {
      return "Validation error";
    }

    return messageSource.getMessage(
        defaultMessage, error.getArguments(), defaultMessage, LocaleContextHolder.getLocale());
  }
}
