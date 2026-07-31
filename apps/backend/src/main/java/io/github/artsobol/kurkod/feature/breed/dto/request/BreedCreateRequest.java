package io.github.artsobol.kurkod.feature.breed.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BreedCreateRequest {

  @NotBlank private String name;

  @NotNull @Positive private Integer eggsNumber;

  @NotNull @Positive private Integer weight;
}
