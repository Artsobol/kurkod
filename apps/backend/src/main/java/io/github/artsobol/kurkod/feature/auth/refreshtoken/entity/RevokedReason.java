package io.github.artsobol.kurkod.feature.auth.refreshtoken.entity;

public enum RevokedReason {
    LOGOUT,
    ROTATED,
    COMPROMISED,
    ADMIN_REVOKE
}
