package io.github.artsobol.kurkod.feature.iam.error;

import io.github.artsobol.kurkod.infrastructure.error.descriptor.ErrorDescriptor;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum AuthError implements ErrorDescriptor {
    AUTHENTICATION_FAILED_FOR_USER("AUTH-401", "auth.authentication.failed", HttpStatus.UNAUTHORIZED),
    INVALID_USER_OR_PASSWORD("AUTH-401", "auth.credentials.invalid", HttpStatus.UNAUTHORIZED),
    INVALID_USER_REGISTRATION_STATUS("AUTH-400", "auth.registration.status.invalid", HttpStatus.BAD_REQUEST),
    CONFIRM_YOUR_EMAIL("AUTH-403-01", "auth.email.confirmation.required", HttpStatus.FORBIDDEN),
    EMAIL_VERIFICATION_TOKEN_NOT_FOUND("AUTH-404", "auth.email.verification.token.not.found", HttpStatus.NOT_FOUND),
    CONFIRMATION_LINK_EXPIRED("AUTH-410", "auth.email.confirmation.expired", HttpStatus.GONE),
    MISMATCH_PASSWORDS("AUTH-400", "auth.password.mismatch", HttpStatus.BAD_REQUEST),
    INVALID_PASSWORD("AUTH-400", "auth.password.invalid", HttpStatus.BAD_REQUEST);

    private final String code;
    private final String messageKey;
    private final HttpStatus status;

    public String format(Object... args) {
        return String.format("[%s] %s", code, String.format(messageKey, args));
    }
}
