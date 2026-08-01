package io.github.artsobol.kurkod.config.security;

import java.time.Duration;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.security.cookie")
public record CookieProperties(
        boolean secure,
        Duration maxAge,
        String sameSite,
        String cookieName,
        String path
) {
}
