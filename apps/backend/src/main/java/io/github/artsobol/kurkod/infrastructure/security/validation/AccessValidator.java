package io.github.artsobol.kurkod.infrastructure.security.validation;

import io.github.artsobol.kurkod.exception.business.InvalidPasswordException;
import io.github.artsobol.kurkod.exception.http.DataExistException;
import io.github.artsobol.kurkod.feature.user.repository.UserRepository;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AccessValidator {

    private static final int MIN_PASSWORD_LENGTH = 8;
    private static final String SPECIAL_CHARACTERS = "~`!@#$%^&*()-_=+[{]}\\|;:'\",<.>/?";

    private final UserRepository userRepository;

    public void validateNewUser(String username, String email, String password, String confirmPassword) {
        userRepository.findByUsernameAndIsActiveTrue(username).ifPresent(u -> {
            throw new DataExistException("user.username.already.exists", username);
        });

        userRepository.findByEmailAndIsActiveTrue(email).ifPresent(u -> {
            throw new DataExistException("user.email.already.exists", email);
        });

        if (!Objects.equals(password, confirmPassword)) {
            throw new InvalidPasswordException("auth.password.mismatch");
        }

        if (isPasswordInvalid(password)) {
            throw new InvalidPasswordException("auth.password.invalid");
        }
    }

    private boolean isPasswordInvalid(String password) {
        if (password == null || password.length() < MIN_PASSWORD_LENGTH) {
            return true;
        }

        boolean hasUpperCase = false;
        boolean hasLowerCase = false;
        boolean hasDigit = false;
        boolean hasSpecialCharacter = false;

        for (char character : password.toCharArray()) {
            if (character >= 'A' && character <= 'Z') {
                hasUpperCase = true;
            } else if (character >= 'a' && character <= 'z') {
                hasLowerCase = true;
            } else if (character >= '0' && character <= '9') {
                hasDigit = true;
            } else if (SPECIAL_CHARACTERS.indexOf(character) >= 0) {
                hasSpecialCharacter = true;
            } else {
                return true;
            }
        }

        return !(hasUpperCase && hasLowerCase && hasDigit && hasSpecialCharacter);
    }
}
