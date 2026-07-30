package io.github.artsobol.kurkod.feature.passport.error;

import io.github.artsobol.kurkod.infrastructure.error.descriptor.ErrorDescriptor;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum PassportError implements ErrorDescriptor {
    NOT_FOUND_BY_WORKER_ID("PAS-404", "passport.not.found.by.worker", HttpStatus.NOT_FOUND),
    ALREADY_EXISTS("PAS-409", "passport.already.exists", HttpStatus.CONFLICT),
    ;

    private final String code;
    private final String messageKey;
    private final HttpStatus status;

    public String format(Object... args) {
        return String.format("[%s] %s", code, String.format(messageKey, args));
    }
}
