package io.github.artsobol.kurkod.feature.dismissal.mapper;

import io.github.artsobol.kurkod.feature.dismissal.dto.response.DismissalDTO;
import io.github.artsobol.kurkod.feature.dismissal.entity.Dismissal;
import io.github.artsobol.kurkod.feature.dismissal.dto.request.DismissalPatchRequest;
import io.github.artsobol.kurkod.feature.dismissal.dto.request.DismissalPostRequest;
import io.github.artsobol.kurkod.feature.dismissal.dto.request.DismissalPutRequest;
import io.github.artsobol.kurkod.feature.worker.entity.Worker;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface DismissalMapper {

    @Mapping(target = "worker", expression = "java(getFullName(dismissal.getWorker()))")
    @Mapping(target = "whoDismiss", expression = "java(getFullName(dismissal.getWhoDismiss()))")
    DismissalDTO toDTO(Dismissal dismissal);

    @Mapping(target = "worker", ignore = true)
    @Mapping(target = "whoDismiss", ignore = true)
    Dismissal toEntity(DismissalPostRequest dismissalPostRequest);

    void  update(@MappingTarget Dismissal dismissal, DismissalPatchRequest dismissalPatchRequest);

    void replace(@MappingTarget Dismissal dismissal, DismissalPutRequest dismissalPutRequest);

    default String getFullName(Worker worker) {
        if (worker == null) return null;
        return worker.getFirstName() + " " + worker.getLastName();
    }
}
