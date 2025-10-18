package io.github.artsobol.kurkod.web.domain.worker.model.request;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class WorkerPostRequest {

    @Column(nullable = false, length = 50)
    @Size(min = 2, max = 50, message = "First name should be between 2 and 50 characters")
    private String firstName;

    @Column(nullable = false, length = 50)
    @Size(min = 2, max = 50, message = "First name should be between 2 and 50 characters")
    private String lastName;
}
