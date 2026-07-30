package io.github.artsobol.kurkod.infrastructure.error.descriptor;

import io.github.artsobol.kurkod.infrastructure.error.descriptor.ErrorDescriptor;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum CommonError implements ErrorDescriptor {
    VALIDATION_FAILED("COM-400-VALIDATION", "common.validation.failed", HttpStatus.BAD_REQUEST),
    MALFORMED_JSON("COM-400-JSON",
                   "common.json.malformed",
                   HttpStatus.BAD_REQUEST),
    UNSUPPORTED_MEDIA_TYPE("COM-415",
                           "common.media.type.unsupported",
                           HttpStatus.UNSUPPORTED_MEDIA_TYPE),
    METHOD_NOT_ALLOWED("COM-405",
                       "common.method.not.allowed",
                       HttpStatus.METHOD_NOT_ALLOWED),
    NOT_ACCEPTABLE("COM-406",
                   "common.not.acceptable",
                   HttpStatus.NOT_ACCEPTABLE),
    MISSING_IF_MATCH("COM-428",
                     "common.if.match.missing",
                     HttpStatus.PRECONDITION_REQUIRED),
    INVALID_IF_MATCH("COM-400-IFM", "common.if.match.invalid", HttpStatus.BAD_REQUEST),
    PRECONDITION_FAILED("COM-412", "common.precondition.failed", HttpStatus.PRECONDITION_FAILED),
    INTERNAL_ERROR("COM-500", "common.internal.error", HttpStatus.INTERNAL_SERVER_ERROR),
    BAD_REQUEST("COM-400", "common.bad.request", HttpStatus.BAD_REQUEST),
    ;

    private final String code;
    private final String messageKey;
    private final HttpStatus status;
}
