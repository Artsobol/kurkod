package io.github.artsobol.kurkod.infrastructure.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.artsobol.kurkod.infrastructure.error.dto.IamError;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
@RequiredArgsConstructor
public class AccessRestrictionHandler implements AccessDeniedHandler {

    private static final HttpStatus STATUS = HttpStatus.FORBIDDEN;
    private static final String ERROR_CODE = "USR-403";
    private static final String MESSAGE_KEY = "user.access.denied";

    private final ObjectMapper objectMapper;
    private final MessageSource messageSource;

    @Override
    @SneakyThrows
    public void handle(HttpServletRequest request,
                       HttpServletResponse response,
                       AccessDeniedException accessDeniedException) {

        Locale locale = LocaleContextHolder.getLocale();
        String message = messageSource.getMessage(
                MESSAGE_KEY,
                null,
                locale
                                                 );

        IamError body = IamError.createError(
                STATUS,
                ERROR_CODE,
                message,
                request.getRequestURI()
                                            );

        response.setStatus(STATUS.value());
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        response.getWriter().write(objectMapper.writeValueAsString(body));
    }
}
