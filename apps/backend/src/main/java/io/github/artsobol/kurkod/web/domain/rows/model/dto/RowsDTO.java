package io.github.artsobol.kurkod.web.domain.rows.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RowsDTO {

    private Integer id;

    private Integer rowNumber;

    private Integer workshopNumber;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
