package io.github.artsobol.kurkod.infrastructure.security.handler;

import io.github.artsobol.kurkod.infrastructure.error.dto.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.support.RequestContextUtils;
import tools.jackson.databind.ObjectMapper;

@Component
@RequiredArgsConstructor
public class AccessRestrictionHandler implements AccessDeniedHandler {

  private static final HttpStatus STATUS = HttpStatus.FORBIDDEN;
  private static final String ERROR_CODE = "USR-403";
  private static final String MESSAGE_KEY = "user.access.denied";

  private final ObjectMapper objectMapper;
  private final MessageSource messageSource;

  @Override
  public void handle(
      HttpServletRequest request,
      HttpServletResponse response,
      AccessDeniedException accessDeniedException)
      throws IOException {

    Locale locale = RequestContextUtils.getLocale(request);
    String message = messageSource.getMessage(MESSAGE_KEY, null, locale);

    ErrorResponse body =
        ErrorResponse.create(STATUS, ERROR_CODE, message, request.getRequestURI());

    response.setStatus(STATUS.value());
    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    response.setCharacterEncoding(StandardCharsets.UTF_8.name());

    response.getWriter().write(objectMapper.writeValueAsString(body));
  }
}
