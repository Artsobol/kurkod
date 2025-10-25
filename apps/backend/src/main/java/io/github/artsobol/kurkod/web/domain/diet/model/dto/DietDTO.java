package io.github.artsobol.kurkod.web.domain.diet.model.dto;

import io.github.artsobol.kurkod.web.domain.common.model.Season;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DietDTO {

    private Integer id;

    private String title;

    private String code;

    private String description;

    private Season season;
}
