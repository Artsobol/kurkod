package io.github.artsobol.kurkod.feature.chicken.dto.request;

import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Positive;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChickenUpdateRequest {

  private String name;

  @Positive private Integer weight;

  @Past private LocalDate birthDate;

  private Long breedId;

  private Long cageId;
}
