package io.github.artsobol.kurkod.feature.cage.dto.request;

import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CageUpdateRequest {

  @Positive private Integer cageNumber;
}
