package io.github.artsobol.kurkod.feature.chicken.dto.request;

import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChickenCreateRequest implements Serializable {

  private String name;

  @NotNull private Integer weight;

  @NotNull private LocalDate birthDate;

  @NotNull private Long breedId;

  @NotNull private Long cageId;
}
