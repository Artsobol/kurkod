package io.github.artsobol.kurkod.security.validation;

import io.github.artsobol.kurkod.error.impl.AuthError;
import io.github.artsobol.kurkod.error.impl.UserError;
import io.github.artsobol.kurkod.model.entity.User;
import io.github.artsobol.kurkod.model.exception.DataExistException;
import io.github.artsobol.kurkod.model.exception.InvalidPasswordException;
import io.github.artsobol.kurkod.model.exception.NotFoundException;
import org.springframework.security.access.AccessDeniedException;
import io.github.artsobol.kurkod.repository.UserRepository;
import io.github.artsobol.kurkod.service.model.ServiceUserRole;
import io.github.artsobol.kurkod.utils.ApiUtils;
import io.github.artsobol.kurkod.utils.PasswordUtils;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

import java.util.Arrays;


@Component
@RequiredArgsConstructor
public class AccessValidator {

    private final UserRepository userRepository;
    private final ApiUtils apiUtils;

    public void validateNewUser(String username, String email, String password, String confirmPassword) {
        userRepository.findByUsernameAndIsActiveTrue(username).ifPresent(u -> {
            throw new DataExistException(UserError.WITH_USERNAME_ALREADY_EXISTS.format(username));
        });

        userRepository.findByEmailAndIsActiveTrue(email).ifPresent(u -> {
            throw new DataExistException(UserError.WITH_EMAIL_ALREADY_EXISTS.format(email));
        });

        if(!password.equals(confirmPassword)) {
            throw new InvalidPasswordException(AuthError.MISMATCH_PASSWORDS.format());
        }

        if (PasswordUtils.isNotValidPassword(password) ) {
            throw new InvalidPasswordException(AuthError.INVALID_PASSWORD.format());
        }
    }

    @SneakyThrows
    public void validateDirectorOrSuperAdmin() {
        Integer currentUserId = apiUtils.getUserIdFromAuthentication();

        if (!hasRole(currentUserId, ServiceUserRole.DIRECTOR, ServiceUserRole.SUPER_ADMIN)) {
            throw new AccessDeniedException(UserError.HAVE_NO_ACCESS.format());
        }
    }

    @SneakyThrows
    public void validateSuperAdminAccess() {
        Integer currentUserId = apiUtils.getUserIdFromAuthentication();

        if (!hasRole(currentUserId, ServiceUserRole.SUPER_ADMIN)) {
            throw new AccessDeniedException(UserError.HAVE_NO_ACCESS.format());
        }
    }

    @SneakyThrows
    public void validateAdminAccess() {
        Integer currentUserId = apiUtils.getUserIdFromAuthentication();

        if (!hasRole(currentUserId, ServiceUserRole.ADMIN, ServiceUserRole.SUPER_ADMIN)) {
            throw new AccessDeniedException(UserError.HAVE_NO_ACCESS.format());
        }
    }

    @SneakyThrows
    public void validateDirectorAccess() {
        Integer currentUserId = apiUtils.getUserIdFromAuthentication();

        if (!hasRole(currentUserId, ServiceUserRole.DIRECTOR)) {
            throw new AccessDeniedException(UserError.HAVE_NO_ACCESS.format());
        }
    }

    public boolean hasRole(Integer userId, ServiceUserRole... roles) {
        User user = getActiveUserById(userId);
        return user.getRoles().stream()
                .map(role -> ServiceUserRole.fromString(role.getName()))
                .anyMatch(userRole -> Arrays.asList(roles).contains(userRole));
    }

    private User getActiveUserById(Integer userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(UserError.NOT_FOUND_BY_ID.format(userId)));
    }
}
