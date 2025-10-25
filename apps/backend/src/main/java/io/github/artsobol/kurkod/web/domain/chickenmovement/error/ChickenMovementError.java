package io.github.artsobol.kurkod.web.domain.chickenmovement.error;

import io.github.artsobol.kurkod.common.error.ErrorDescriptor;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ChickenMovementError implements ErrorDescriptor {
    NOT_FOUND_BY_ID("CHM-404", "chicken_movement.chicken_movement_not_found_by_id", HttpStatus.NOT_FOUND),
    ALREADY_EXISTS("CHM-409", "chicken_movement.chicken_movement_already_exists", HttpStatus.CONFLICT),
    ;

    private final String code;
    private final String messageKey;
    private final HttpStatus status;

    public String format(Object... args) {
        return String.format("[%s] %s", code, String.format(messageKey, args));
    }
}

