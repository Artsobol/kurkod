package io.github.artsobol.kurkod.infrastructure.security.config;

import java.util.List;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.security.security-config")
public record SecurityConfigProperties(List<String> allowedOrigins, Long maxAge) {
  public SecurityConfigProperties {
    allowedOrigins = List.copyOf(allowedOrigins);
  }
}
