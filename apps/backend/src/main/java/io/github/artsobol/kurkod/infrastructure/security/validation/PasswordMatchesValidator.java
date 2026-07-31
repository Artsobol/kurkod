package io.github.artsobol.kurkod.infrastructure.security.validation;

import io.github.artsobol.kurkod.feature.iam.dto.request.RegistrationRequest;
import io.github.artsobol.kurkod.infrastructure.validation.PasswordMatches;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordMatchesValidator
    implements ConstraintValidator<PasswordMatches, RegistrationRequest> {
  @Override
  public boolean isValid(
      RegistrationRequest registrationRequest,
      ConstraintValidatorContext constraintValidatorContext) {
    return registrationRequest.getPassword().equals(registrationRequest.getConfirmPassword());
  }
}
