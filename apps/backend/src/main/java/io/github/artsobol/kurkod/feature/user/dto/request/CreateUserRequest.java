package io.github.artsobol.kurkod.feature.user.dto.request;

public record CreateUserRequest(
        String username,
        String email,
        String passwordHash
) {}
