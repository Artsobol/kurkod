package io.github.artsobol.kurkod.feature.iam.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.context.i18n.LocaleContextHolder;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoleResponse {

    private Long id;
    private String name;
}
