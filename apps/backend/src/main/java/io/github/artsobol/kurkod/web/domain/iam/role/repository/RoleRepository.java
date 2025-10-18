package io.github.artsobol.kurkod.web.domain.iam.role.repository;

import io.github.artsobol.kurkod.web.domain.iam.role.model.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {

    Optional<Role> findByName(String name);
}
