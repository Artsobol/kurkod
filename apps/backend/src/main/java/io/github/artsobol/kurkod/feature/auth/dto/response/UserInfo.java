package io.github.artsobol.kurkod.feature.auth.dto.response;

import io.github.artsobol.kurkod.feature.user.entity.Role;

public record UserInfo(Long userId, String username, Role role) {}
