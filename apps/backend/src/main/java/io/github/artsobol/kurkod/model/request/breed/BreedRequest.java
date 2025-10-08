package io.github.artsobol.kurkod.model.request.breed;

import lombok.*;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BreedRequest implements Serializable {

    private String name;

    private short eggsNumber;

    private short weight;
}
