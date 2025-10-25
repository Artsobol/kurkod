package io.github.artsobol.kurkod.web.domain.dismissal.model.dto;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DismissalDTO {

    private Integer id;

    private LocalDate dismissalDate;

    private String reason;

    private String worker;

    private String whoDismiss;
}
