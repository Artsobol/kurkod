package io.github.artsobol.kurkod.repository;

import io.github.artsobol.kurkod.model.entity.User;
import io.github.artsobol.kurkod.model.enums.RegistrationStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findByIdAndIsActiveTrue(int id);

    List<User> findAllByIsActiveTrue();

    Optional<User> findByUsernameAndIsActiveTrue(String username);

    Optional<User> findByEmailAndIsActiveTrue(String email);

    Optional<User> findByEmail(String email);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);
}
