package io.github.artsobol.kurkod.mapper;

import io.github.artsobol.kurkod.model.dto.worker.WorkerDTO;
import io.github.artsobol.kurkod.model.entity.Worker;
import io.github.artsobol.kurkod.model.request.worker.WorkerPatchRequest;
import io.github.artsobol.kurkod.model.request.worker.WorkerPostRequest;
import io.github.artsobol.kurkod.model.request.worker.WorkerPutRequest;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring",
nullValuePropertyMappingStrategy = org.mapstruct.NullValuePropertyMappingStrategy.IGNORE)
public interface WorkerMapper {

    WorkerDTO toDTO(Worker worker);

    Worker toEntity(WorkerPostRequest workerPostRequest);

    void updateFully(@MappingTarget Worker worker, WorkerPutRequest workerPutRequest);

    void updatePartially(@MappingTarget Worker worker, WorkerPatchRequest workerPatchRequest);
}
