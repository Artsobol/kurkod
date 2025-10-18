package io.github.artsobol.kurkod.web.domain.breed.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BreedPutRequest implements Serializable {

    @NotBlank
    private String name;

    @NotNull
    private Integer eggsNumber;

    @NotNull
    private Integer weight;
}
