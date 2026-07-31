package io.github.artsobol.kurkod.feature.staff.repository;

import io.github.artsobol.kurkod.feature.staff.entity.Staff;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StaffRepository extends JpaRepository<Staff, Long> {

    Optional<Staff> findStaffByIdAndIsActiveTrue(Long id);

    List<Staff> findAllByIsActiveTrue();
    Page<Staff> findAllByIsActiveTrue(Pageable pageable);
}
