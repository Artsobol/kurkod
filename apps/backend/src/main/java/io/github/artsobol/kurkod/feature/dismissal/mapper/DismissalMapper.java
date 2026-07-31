package io.github.artsobol.kurkod.feature.dismissal.mapper;

import io.github.artsobol.kurkod.feature.dismissal.dto.response.DismissalResponse;
import io.github.artsobol.kurkod.feature.dismissal.entity.Dismissal;
import io.github.artsobol.kurkod.feature.dismissal.dto.request.DismissalUpdateRequest;
import io.github.artsobol.kurkod.feature.dismissal.dto.request.DismissalCreateRequest;
import io.github.artsobol.kurkod.feature.worker.entity.Worker;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface DismissalMapper {

    @Mapping(target = "worker", expression = "java(getFullName(dismissal.getWorker()))")
    @Mapping(target = "whoDismiss", expression = "java(getFullName(dismissal.getWhoDismiss()))")
    DismissalResponse toResponse(Dismissal dismissal);

    @Mapping(target = "worker", ignore = true)
    @Mapping(target = "whoDismiss", ignore = true)
    Dismissal toEntity(DismissalCreateRequest dismissalCreateRequest);

    void  update(@MappingTarget Dismissal dismissal, DismissalUpdateRequest dismissalUpdateRequest);
    default String getFullName(Worker worker) {
        if (worker == null) return null;
        return worker.getFirstName() + " " + worker.getLastName();
    }
}
