package io.github.artsobol.kurkod.feature.rows.dto.request;

import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RowsUpdateRequest {

  @Positive private Integer rowNumber;
}
