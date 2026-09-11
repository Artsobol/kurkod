package io.github.artsobol.kurkod.feature.dismissal.dto.request;

import jakarta.validation.constraints.*;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DismissalCreateRequest {

  @NotNull @PastOrPresent private LocalDate dismissalDate;

  @Size(max = 200) private String reason;

  @NotNull @Positive private Long workerId;
}
