package io.github.artsobol.kurkod.web.domain.cage.model.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CagePostRequest {

    @Min(1)
    @NotNull
    private Integer cageNumber;
}
