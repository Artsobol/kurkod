package io.github.artsobol.kurkod.feature.breed.dto.request;

import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BreedUpdateRequest implements Serializable {

    private String name;

    @Positive
    private Integer eggsNumber;

    @Positive
    private Integer weight;
}
