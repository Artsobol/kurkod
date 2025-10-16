package io.github.artsobol.kurkod.security.validation;

import io.github.artsobol.kurkod.model.request.user.RegistrationUserRequest;
import io.github.artsobol.kurkod.utils.PasswordMatches;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordMatchesValidator implements ConstraintValidator<PasswordMatches, RegistrationUserRequest> {
    @Override
    public boolean isValid(RegistrationUserRequest registrationUserRequest, ConstraintValidatorContext constraintValidatorContext) {
        return registrationUserRequest.getPassword().equals(registrationUserRequest.getConfirmPassword());
    }
}
