package io.github.artsobol.kurkod.feature.iam.error;

import io.github.artsobol.kurkod.infrastructure.error.descriptor.ErrorDescriptor;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum UserError implements ErrorDescriptor {
    NOT_FOUND_BY_ID("USR-404", "user.not.found.by.id", HttpStatus.NOT_FOUND),
    NOT_FOUND_BY_USERNAME("USR-404", "user.not.found.by.username", HttpStatus.NOT_FOUND),
    NOT_FOUND_BY_EMAIL("USR-404", "user.not.found.by.email", HttpStatus.NOT_FOUND),
    WITH_USERNAME_ALREADY_EXISTS("USR-409", "user.username.already.exists", HttpStatus.CONFLICT),
    WITH_EMAIL_ALREADY_EXISTS("USR-409","user.email.already.exists", HttpStatus.CONFLICT),
    HAVE_NO_ACCESS("USR-403", "user.access.denied", HttpStatus.FORBIDDEN),
    ;

    private final String code;
    private final String messageKey;
    private final HttpStatus status;

    public String format(Object... args) {
        return String.format("[%s] %s", code, String.format(messageKey, args));
    }
}
