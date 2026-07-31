package io.github.artsobol.kurkod.feature.diet.dto.request;

import io.github.artsobol.kurkod.feature.diet.entity.Season;
import jakarta.validation.constraints.Size;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DietUpdateRequest {

    Set<Integer> addBreedsIds;
    Set<Integer> removeBreedsIds;
    @Size(min=2, max=30)
    private String title;
    @Size(min=2, max=10)
    private String code;
    private String description;
    private Season season;
}
