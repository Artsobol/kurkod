package io.github.artsobol.kurkod.model.request.breed;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BreedPostRequest implements Serializable {

    @NotBlank
    private String name;

    @NotNull
    private short eggsNumber;

    @NotNull
    private short weight;
}
