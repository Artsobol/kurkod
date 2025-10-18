package io.github.artsobol.kurkod.web.domain.breed.model.request;

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
    private Integer eggsNumber;

    @NotNull
    private Integer weight;
}
