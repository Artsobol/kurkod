package io.github.artsobol.kurkod.model.request.staff;

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
public class StaffPutRequest {

    @NotNull
    @Size(min = 2, max = 50, message = "Name should be between 2 and 50 characters")
    private String position;
}
