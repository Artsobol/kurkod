package io.github.artsobol.kurkod.feature.auth.dto.request;

public record SessionMetadata(String ipAddress, String userAgent, String deviceName) {}
