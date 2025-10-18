package io.github.artsobol.kurkod.web.domain.staff.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class StaffDTO {

    private Integer id;

    private String position;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
