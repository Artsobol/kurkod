package io.github.artsobol.kurkod.web.domain.chicken.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChickenPatchRequest {

    private String name;

    private Integer weight;

    private LocalDate birthDate;

    private Integer breedId;
}
