package io.github.artsobol.kurkod.feature.workshop.entity;

import io.github.artsobol.kurkod.feature.rows.entity.Rows;
import io.github.artsobol.kurkod.infrastructure.persistence.entity.AbstractEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Positive;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "workshop")
public class Workshop extends AbstractEntity {

    @Positive
    @Column(nullable = false, unique = true, name = "workshop_number")
    private Integer workshopNumber;

    @OneToMany(mappedBy = "workshop", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Rows> rows;
}
