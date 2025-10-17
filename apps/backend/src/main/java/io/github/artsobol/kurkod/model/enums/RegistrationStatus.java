package io.github.artsobol.kurkod.model.enums;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public enum RegistrationStatus {
    ACTIVE,
    INACTIVE,
    PENDING_CONFIRMATION
}
