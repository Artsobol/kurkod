package io.github.artsobol.kurkod.feature.diet.dto.request;

import io.github.artsobol.kurkod.infrastructure.persistence.entity.Season;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DietPatchRequest {

    @Size(min=2, max=30)
    private String title;

    @Size(min=2, max=10)
    private String code;

    private String description;

    private Season season;

    Set<Integer> addBreedsIds;

    Set<Integer> removeBreedsIds;
}
