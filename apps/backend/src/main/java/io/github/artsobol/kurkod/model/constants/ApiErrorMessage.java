package io.github.artsobol.kurkod.model.constants;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum ApiErrorMessage {
    BREED_NOT_FOUND_BY_ID("Breed with ID: %s was not found"),
    BREED_ALREADY_EXISTS("Breed with name: %s already exists"),
    USER_NOT_FOUND_BY_ID("User with ID: %s was not found"),
    USER_NOT_FOUND_BY_USERNAME("User with username: %s was not found"),
    USER_NOT_FOUND_BY_EMAIL("User with email: %s was not found"),
    USER_WITH_USERNAME_ALREADY_EXISTS("User with username: %s already exists"),
    USER_WITH_EMAIL_ALREADY_EXISTS("User with email: %s already exists"),
    ERROR_DURING_JWT_PROCESSING("An unexpected error occurred during JWT processing"),
    TOKEN_EXPIRED("Token has expired"),
    UNEXPECTED_ERROR_OCCURRED("An unexpected error occurred. Please try again later"),
    INVALID_TOKEN_SIGNATURE("Error"),

    AUTHENTICATION_FAILED_FOR_USER("Authentication failed for user: {}. "),
    INVALID_USER_OR_PASSWORD("Invalid email or password. Try again"),
    INVALID_USER_REGISTRATION_STATUS("Invalid user registration status: %s. "),
    NOT_FOUND_REFRESH_TOKEN("Refresh token not found."),
    CONFIRM_YOUR_EMAIL("Please confirm your email before login"),
    EMAIL_VERIFICATION_TOKEN_NOT_FOUND("Email verification token not found"),
    CONFIRMATION_LINK_EXPIRED("The confirmation link has expired. Please request a new one"),

    MISMATCH_PASSWORDS("Password does not match"),
    INVALID_PASSWORD("Invalid password. It must have: "
            + "length at least " + ApiConstants.REQUIRED_MIN_PASSWORD_LENGTH + ", including "
            + ApiConstants.REQUIRED_MIN_LETTERS_NUMBER_EVERY_CASE_IN_PASSWORD + " letter(s) in upper and lower cases, "
            + ApiConstants.REQUIRED_MIN_CHARACTERS_NUMBER_IN_PASSWORD + " character(s), "
            + ApiConstants.REQUIRED_MIN_DIGITS_NUMBER_IN_PASSWORD + " digit(s). "),

    HAVE_NO_ACCESS("You don't have access to this resource"),

            ;
    private final String message;

    public String getMessage(Object... args) {
        return String.format(message, args);
    }
}
