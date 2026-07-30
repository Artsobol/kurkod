package io.github.artsobol.kurkod.feature.rows.error;

import io.github.artsobol.kurkod.infrastructure.error.descriptor.ErrorDescriptor;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum RowsError implements ErrorDescriptor {
    NOT_FOUND_BY_ID("RS-404", "row.not.found", HttpStatus.NOT_FOUND),
    NOT_FOUND_BY_KEYS("RS-404", "row.not.found.by.keys", HttpStatus.NOT_FOUND),
    ALREADY_EXISTS("RS-409", "row.already.exists", HttpStatus.CONFLICT),
    ;

    private final String code;
    private final String messageKey;
    private final HttpStatus status;

    public String format(Object... args) {
        return String.format("[%s] %s", code, String.format(messageKey, args));
    }
}
