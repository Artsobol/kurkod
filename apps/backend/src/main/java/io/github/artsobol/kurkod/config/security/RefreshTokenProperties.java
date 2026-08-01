package io.github.artsobol.kurkod.config.security;

import java.time.Duration;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.security.refresh-token")
public record RefreshTokenProperties(
        Duration ttl,
        String pepper,
        int length
) {
}
