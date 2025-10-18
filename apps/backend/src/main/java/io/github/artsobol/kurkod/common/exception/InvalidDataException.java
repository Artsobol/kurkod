package io.github.artsobol.kurkod.common.exception;

import org.springframework.http.HttpStatus;

/**
 * Thrown when the provided data is invalid or does not meet validation rules.
 * Example: missing required fields, invalid email format, etc.
 */
public class InvalidDataException extends BaseException{

    public InvalidDataException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
