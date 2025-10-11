package io.github.artsobol.kurkod.model.dto.worker;

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
}
