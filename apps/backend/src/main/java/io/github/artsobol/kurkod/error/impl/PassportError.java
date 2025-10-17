package io.github.artsobol.kurkod.error.impl;

import io.github.artsobol.kurkod.error.ErrorDescriptor;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum PassportError implements ErrorDescriptor {
    NOT_FOUND_BY_ID("PAS-404", "passport.passport_not_found_by_id", HttpStatus.NOT_FOUND),
    ;

    private final String code;
    private final String messageKey;
    private final HttpStatus status;

    public String format(Object... args) {
        return String.format("[%s] %s", code, String.format(messageKey, args));
    }
}
