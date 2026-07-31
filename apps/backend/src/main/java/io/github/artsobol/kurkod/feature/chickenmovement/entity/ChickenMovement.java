package io.github.artsobol.kurkod.feature.chickenmovement.entity;

import io.github.artsobol.kurkod.feature.cage.entity.Cage;
import io.github.artsobol.kurkod.feature.chicken.entity.Chicken;
import io.github.artsobol.kurkod.infrastructure.persistence.entity.AbstractEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "chicken_movement")
public class ChickenMovement extends AbstractEntity {

    @NotNull @Column(nullable = false, name = "moved_at") private Instant movedAt = Instant.now();

    @ManyToOne(fetch = FetchType.LAZY, optional = false) @JoinColumn(name = "chicken_id", nullable = false)
    private Chicken chicken;

    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "from_cage_id") private Cage fromCage;

    @ManyToOne(fetch = FetchType.LAZY, optional = false) @JoinColumn(name = "to_cage_id", nullable = false)
    private Cage toCage;
}
