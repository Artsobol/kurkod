package io.github.artsobol.kurkod.model.request.breed;

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

    private short eggsNumber;

    private short weight;
}
