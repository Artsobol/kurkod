package io.github.artsobol.kurkod.model.dto.passport;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PassportDTO {

    private String series;

    private String number;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
