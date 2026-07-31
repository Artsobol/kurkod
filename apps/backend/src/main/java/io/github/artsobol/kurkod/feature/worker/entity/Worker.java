package io.github.artsobol.kurkod.feature.worker.entity;

import io.github.artsobol.kurkod.infrastructure.persistence.entity.AbstractEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "worker")
@Getter
@Setter
public class Worker extends AbstractEntity {

    @NotBlank
    @Column(nullable = false, length = 30)
    @Size(max = 30, message = "First name should be between 1 and 50 characters")
    private String firstName;

    @NotBlank
    @Column(nullable = false, length = 30)
    @Size(max = 30, message = "Last name should be between 1 and 50 characters")
    private String lastName;

    @Column(length = 30)
    @Size(max = 30, message = "Patronymic should be less than 30 characters")
    private String patronymic;

    @Column(nullable = false, unique = true)
    private String phoneNumber;

    @Email
    @Column(nullable = false, unique = true)
    private String email;

    @OneToMany(mappedBy = "worker", fetch = FetchType.LAZY)
    private Set<WorkerCage> workerCages = new HashSet<>();
}
