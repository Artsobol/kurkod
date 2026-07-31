package io.github.artsobol.kurkod.feature.dismissal.entity;

import io.github.artsobol.kurkod.feature.worker.entity.Worker;
import io.github.artsobol.kurkod.infrastructure.persistence.entity.AbstractEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "dismissal")
@NoArgsConstructor
@AllArgsConstructor
public class Dismissal extends AbstractEntity {

    @NotNull
    @Column(nullable = false, name = "dismissal_date")
    private LocalDate dismissalDate;

    @NotBlank
    @Column(nullable = false)
    private String reason;

    @ManyToOne(optional = false)
    @JoinColumn(name = "worker_id", nullable = false)
    private Worker worker;

    @ManyToOne(optional = false)
    @JoinColumn(name = "who_dismiss_id", nullable = false)
    private Worker whoDismiss;
}
