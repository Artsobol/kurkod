package io.github.artsobol.kurkod.infrastructure.error.descriptor;

import io.github.artsobol.kurkod.infrastructure.error.descriptor.ErrorDescriptor;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum RequiredHeaderError implements ErrorDescriptor {
    IF_MATCH("IFM-428", "common.if.match.missing", HttpStatus.PRECONDITION_REQUIRED),
    MATCH_FAILED("IFM-412", "common.version.mismatch", HttpStatus.PRECONDITION_FAILED),
    MATCH_INVALID("IFM-400", "common.if.match.invalid", HttpStatus.BAD_REQUEST),
    ;

    private final String code;
    private final String messageKey;
    private final HttpStatus status;

    public String format(Object... args) {
        return String.format("[%s] %s", code, String.format(messageKey, args));
    }
}
