package io.github.artsobol.kurkod.infrastructure.security.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@ConfigurationProperties(prefix = "app.security-config")
public record SecurityConfigProperties(
        List<String> allowedOrigins,
        Long maxAge
) {}
