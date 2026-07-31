package io.github.artsobol.kurkod.feature.staff.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StaffCreateRequest {

    @NotNull
    @Size(min = 2, max = 50, message = "Name should be between 2 and 50 characters")
    private String position;
}
