package io.github.artsobol.kurkod.infrastructure.error.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@Builder
@Schema(description = "Error response API")
public class IamError {
    private final LocalDateTime time = LocalDateTime.now();
    private int status;
    private String error;
    private String code;
    private String message;
    private String path;
    private List<String> details;

    public static IamError createError(HttpStatus status, String code, String message, String path) {
        return IamError.builder()
                .status(status.value())
                .code(code)
                .error(status.getReasonPhrase())
                .message(message)
                .path(path)
                .build();
    }
}
