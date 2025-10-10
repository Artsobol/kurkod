package io.github.artsobol.kurkod.model.constants;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum ApiLogMessage {
    BREED_INFO_BY_ID("Receiving breed with ID: {}"),
    NAME_OF_CURRENT_METHOD("Current method: {}")
    ;

    private final String value;
}
