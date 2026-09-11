package io.github.artsobol.kurkod.feature.user.repository;

import io.github.artsobol.kurkod.feature.user.entity.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

  Optional<User> findByIdAndIsActiveTrue(Long id);

  List<User> findAllByIsActiveTrue();

  Optional<User> findByUsernameAndIsActiveTrue(String username);

  Optional<User> findByEmailAndIsActiveTrue(String email);

  Optional<User> findByEmail(String email);

  boolean existsByUsername(String username);

  boolean existsByEmail(String email);
}
