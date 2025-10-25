package io.github.artsobol.kurkod.web.domain.eggproductionmonth.model.entity;

import io.github.artsobol.kurkod.web.domain.chicken.model.entity.Chicken;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(
        name = "egg_production_month",
        uniqueConstraints = @UniqueConstraint(
                name = "uq_egg_production_month_year",
                columnNames = {"chicken_id", "year", "month"}
        )
)
@NoArgsConstructor
@AllArgsConstructor
public class EggProductionMonth {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Min(2000)
    @NotNull
    @Column(nullable = false)
    private Integer year;

    @Min(1)
    @Min(12)
    @NotNull
    @Column(nullable = false)
    private Integer month;

    @NotNull
    @Positive
    @Column(nullable = false)
    private Integer eggsCount;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "chicken_id", nullable = false)
    private Chicken chicken;

    @NotNull
    @Column(nullable = false, name = "is_active")
    private boolean isActive = true;

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
