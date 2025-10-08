package io.github.artsobol.kurkod.model.constants;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum ApiErrorMessage {
    BREED_NOT_FOUND_BY_ID("Breed with ID: %s was not found"),
    BREED_ALREADY_EXISTS("Breed with name: %s already exists")
    ;
    private final String message;

    public String getMessage(Object... args) {
        return String.format(message, args);
    }
}
