package io.github.artsobol.kurkod.config.security;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.security.session")
public record SessionProperties(
        long maxSessions
) {
}
