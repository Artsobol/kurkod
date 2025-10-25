package io.github.artsobol.kurkod.web.domain.chicken.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChickenDTO implements Serializable {

    private Integer id;

    private String name;

    private Integer weight;

    private LocalDate birthDate;

    private Integer breedId;
}
