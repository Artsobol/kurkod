package io.github.artsobol.kurkod.model.request.worker;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WorkerPatchRequest {

    private String firstName;

    private String lastName;
}
