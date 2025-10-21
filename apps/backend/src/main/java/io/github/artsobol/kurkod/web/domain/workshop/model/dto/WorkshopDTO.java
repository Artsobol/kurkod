package io.github.artsobol.kurkod.web.domain.workshop.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
public class WorkshopDTO {

    private Integer id;

    private Integer workshopNumber;
}
