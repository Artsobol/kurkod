package io.github.artsobol.kurkod.web.domain.cage.model.entity;

import io.github.artsobol.kurkod.web.domain.common.BaseEntity;
import io.github.artsobol.kurkod.web.domain.rows.model.entity.Rows;
import jakarta.persistence.*;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "cage",
       uniqueConstraints = @UniqueConstraint(name = "uq_cage_row_id_cage_number", columnNames = {"row_id", "cage_number"}))
public class Cage extends BaseEntity {

    @Positive
    @Column(nullable = false, name = "cage_number")
    private Integer cageNumber;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "row_id", nullable = false, referencedColumnName = "id")
    private Rows row;
}
