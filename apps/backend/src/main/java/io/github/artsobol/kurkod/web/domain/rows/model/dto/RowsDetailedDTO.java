package io.github.artsobol.kurkod.web.domain.rows.model.dto;

import io.github.artsobol.kurkod.web.domain.cage.model.entity.Cage;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RowsDetailedDTO {


    private Integer id;

    private Integer rowNumber;

    private Integer workshopNumber;

    private List<Cage> cages;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
