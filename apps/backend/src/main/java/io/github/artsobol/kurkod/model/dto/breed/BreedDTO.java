package io.github.artsobol.kurkod.model.dto.breed;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BreedDTO implements Serializable {
    private Integer id;
    private String name;
    private Integer eggsNumber;
    private Integer weight;
}
