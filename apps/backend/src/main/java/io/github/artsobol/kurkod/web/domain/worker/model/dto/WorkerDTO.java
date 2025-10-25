package io.github.artsobol.kurkod.web.domain.worker.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class WorkerDTO {

    private Integer id;

    private String firstName;

    private String lastName;

    private String patronymic;
}
