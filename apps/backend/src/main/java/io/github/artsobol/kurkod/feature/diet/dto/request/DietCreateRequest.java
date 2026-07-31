package io.github.artsobol.kurkod.feature.diet.dto.request;

import io.github.artsobol.kurkod.feature.diet.entity.Season;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.Set;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DietCreateRequest {

    Set<Integer> breedIds;
    @NotBlank
    @Size(min=2, max=30)
    private String title;
    @NotBlank
    @Size(min=2, max=10)
    private String  code;
    private String description;
    @NotNull
    private Season season;
}
