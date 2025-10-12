package io.github.artsobol.kurkod.model.request.passport;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PassportPutRequest {

    @Column(length = 4, nullable = false)
    @Pattern(regexp = "^[0-9]{4}$", message = "Invalid passport series")
    private String series;

    @Column(length = 6, nullable = false)
    @Pattern(regexp = "^[0-9]{6}$", message = "Invalid passport number")
    private String number;
}