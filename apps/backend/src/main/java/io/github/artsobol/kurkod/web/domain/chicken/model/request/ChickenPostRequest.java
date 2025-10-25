package io.github.artsobol.kurkod.web.domain.chicken.model.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChickenPostRequest implements Serializable {

    @NotNull
    private Integer weight;

    private String name;

    @NotNull
    private LocalDate birthDate;

    @NotNull
    private Integer breedId;
}
