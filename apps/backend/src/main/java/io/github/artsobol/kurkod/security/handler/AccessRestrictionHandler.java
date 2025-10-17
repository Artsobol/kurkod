package io.github.artsobol.kurkod.security.handler;

import io.github.artsobol.kurkod.error.impl.UserError;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

@Component
public class AccessRestrictionHandler implements AccessDeniedHandler {
    @Override
    @SneakyThrows
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) {
        response.setStatus(HttpStatus.FORBIDDEN.value());
        response.getWriter().write(UserError.HAVE_NO_ACCESS.format());
    }
}
