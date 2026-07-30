package io.github.artsobol.kurkod.feature.employmentcontract.entity;

import io.github.artsobol.kurkod.infrastructure.persistence.entity.BaseEntity;
import io.github.artsobol.kurkod.feature.staff.entity.Staff;
import io.github.artsobol.kurkod.feature.worker.entity.Worker;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "employment_contract")
public class EmploymentContract extends BaseEntity {

    @NotBlank
    @Column(nullable = false, unique = true, length = 20)
    @Size(max = 20, message = "Contract number should be less than 20 characters")
    private String contractNumber;

    @NotNull
    @Column(nullable = false)
    private Integer salary;

    @ManyToOne()
    @JoinColumn(name = "staff_id", nullable = false)
    private Staff staff;

    @ManyToOne()
    @JoinColumn(name = "worker_id", nullable = false)
    private Worker worker;

    @NotNull
    @Column(nullable = false, name = "start_date")
    private LocalDate startDate;

    @NotNull
    @Column(nullable = false, name = "end_date")
    private LocalDate endDate;
}
