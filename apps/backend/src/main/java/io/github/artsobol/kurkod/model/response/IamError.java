package io.github.artsobol.kurkod.model.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@Schema(description = "Error response API")
public class IamError {
    private int status;
    private String error;
    private String message;
    private String path;
    private final LocalDateTime time = LocalDateTime.now();
    private List<String> details;

    public static IamError createError(HttpStatus status, String message, String path) {
        return IamError.builder()
                .status(status.value())
                .error(status.getReasonPhrase())
                .message(message)
                .path(path)
                .build();
    }

    public static IamError validationError(List<String> fieldErrors, String path) {
        return IamError.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .error(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .message("Validation failed")
                .details(fieldErrors)
                .path(path)
                .build();
    }
}
