package io.github.artsobol.kurkod.feature.cage.error;

import io.github.artsobol.kurkod.infrastructure.error.descriptor.ErrorDescriptor;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;


@AllArgsConstructor
@Getter
public enum CageError implements ErrorDescriptor {
    NOT_FOUND_BY_KEYS("CG-404", "cage.not.found.by.keys", HttpStatus.NOT_FOUND),
    ALREADY_EXISTS("CG-409", "cage.already.exists", HttpStatus.CONFLICT),
    NOT_FOUND_BY_ID("CG-404", "cage.not.found", HttpStatus.NOT_FOUND),
    ;

    private final String code;
    private final String messageKey;
    private final HttpStatus status;

    public String format(Object... args) {
        return String.format("[%s] %s", code, String.format(messageKey, args));
    }
}
