package io.github.artsobol.kurkod.web.domain.dismissal.model.entity;

import io.github.artsobol.kurkod.web.domain.worker.model.entity.Worker;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "dismissal")
@NoArgsConstructor
@AllArgsConstructor
public class Dismissal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

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

    @NotNull
    @Column(nullable = false, updatable = false, name = "created_at")
    private LocalDateTime createdAt;

    @NotNull
    @Column(nullable = false, name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        LocalDateTime now = LocalDateTime.now();
        this.createdAt = now;
        this.updatedAt = now;
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
