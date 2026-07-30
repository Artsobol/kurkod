package io.github.artsobol.kurkod.infrastructure.security.validation;

import io.github.artsobol.kurkod.feature.iam.error.AuthError;
import io.github.artsobol.kurkod.feature.iam.error.UserError;
import io.github.artsobol.kurkod.exception.http.DataExistException;
import io.github.artsobol.kurkod.exception.business.InvalidPasswordException;
import io.github.artsobol.kurkod.feature.iam.repository.UserRepository;
import io.github.artsobol.kurkod.infrastructure.util.PasswordUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class AccessValidator {

    private final UserRepository userRepository;

    public void validateNewUser(String username, String email, String password, String confirmPassword) {
        userRepository.findByUsernameAndIsActiveTrue(username).ifPresent(u -> {
            throw new DataExistException("user.username.already.exists", username);
        });

        userRepository.findByEmailAndIsActiveTrue(email).ifPresent(u -> {
            throw new DataExistException("user.email.already.exists", email);
        });

        if(!password.equals(confirmPassword)) {
            throw new InvalidPasswordException("auth.password.mismatch", confirmPassword);
        }

        if (PasswordUtils.isNotValidPassword(password) ) {
            throw new InvalidPasswordException("auth.password.invalid", password);
        }
    }
}
