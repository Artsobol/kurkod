package io.github.artsobol.kurkod.web.domain.breed.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BreedPatchRequest implements Serializable {

    private String name;

    private Integer eggsNumber;

    private Integer weight;
}
