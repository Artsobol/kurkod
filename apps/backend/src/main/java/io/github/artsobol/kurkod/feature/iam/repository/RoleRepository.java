package io.github.artsobol.kurkod.feature.iam.repository;

import io.github.artsobol.kurkod.feature.iam.entity.Role;
import io.github.artsobol.kurkod.feature.iam.entity.SystemRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByName(String name);

    Optional<Role> findByUserSystemRole(SystemRole role);
}
