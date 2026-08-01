package io.github.artsobol.kurkod.infrastructure.security.jwt;

import io.github.artsobol.kurkod.infrastructure.error.dto.ErrorResponse;
import io.github.artsobol.kurkod.infrastructure.localization.MessageService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.access.AccessDeniedHandler;
import tools.jackson.databind.ObjectMapper;

@Slf4j
@RequiredArgsConstructor
public class JwtAccessDeniedHandler implements AccessDeniedHandler {

    private final MessageService messageService;
    private final ObjectMapper objectMapper;

    @Override
    public void handle(
            @NonNull HttpServletRequest request,
            HttpServletResponse response,
            @NonNull AccessDeniedException accessDeniedException
    ) throws IOException {
        if (response.isCommitted()) {
            return;
        }

        String message = messageService.createMessage("auth.access.denied", null);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication != null ? authentication.getName() : "anonymous";
        String role = authentication != null ? authentication
                .getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .findFirst()
                .orElse("Unknown") : "Unknown";
        log.warn(
                "Access denied: method={}, URI={}, user={}, role={}, IP={}",
                request.getMethod(),
                request.getRequestURI(),
                username,
                role,
                request.getRemoteAddr()
        );


        response.setStatus(HttpServletResponse.SC_FORBIDDEN);

        ErrorResponse errorResponse = new ErrorResponse(
                java.time.Instant.now(),
                HttpServletResponse.SC_FORBIDDEN,
                "Forbidden",
                "ACCESS_DENIED",
                message,
                request.getRequestURI()
        );

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        objectMapper.writeValue(response.getWriter(), errorResponse);
    }
}
