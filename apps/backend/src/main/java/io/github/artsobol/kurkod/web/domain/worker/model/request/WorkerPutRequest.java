package io.github.artsobol.kurkod.web.domain.worker.model.request;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WorkerPutRequest {

    @NotNull
    @Column(nullable = false, length = 50)
    @Size(min = 2, max = 50, message = "First name should be between 2 and 50 characters")
    private String firstName;

    @NotNull
    @Column(nullable = false, length = 50)
    @Size(min = 2, max = 50, message = "First name should be between 2 and 50 characters")
    private String lastName;
}
