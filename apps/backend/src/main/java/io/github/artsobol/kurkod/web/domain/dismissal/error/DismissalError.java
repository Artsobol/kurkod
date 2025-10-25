package io.github.artsobol.kurkod.web.domain.dismissal.error;

import io.github.artsobol.kurkod.common.error.ErrorDescriptor;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum DismissalError implements ErrorDescriptor {
    NOT_FOUND_BY_ID("DE-404", "dismissal.dismissal_not_found_by_id", HttpStatus.NOT_FOUND),
    ALREADY_EXISTS("DE-409", "dismissal.dismissal_already_exists", HttpStatus.CONFLICT),
    ;

    private final String code;
    private final String messageKey;
    private final HttpStatus status;

    public String format(Object... args) {
        return String.format("[%s] %s", code, String.format(messageKey, args));
    }
}