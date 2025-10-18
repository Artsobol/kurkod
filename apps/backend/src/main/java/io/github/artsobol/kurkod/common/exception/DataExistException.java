package io.github.artsobol.kurkod.common.exception;

import org.springframework.http.HttpStatus;

/**
 * Thrown when attempting to create or save data that already exists in the system.
 * Example: trying to register a user with an email that is already taken.
 */
public class DataExistException extends BaseException{

    public DataExistException(String message) {
        super(message, HttpStatus.CONFLICT  );
    }


}
