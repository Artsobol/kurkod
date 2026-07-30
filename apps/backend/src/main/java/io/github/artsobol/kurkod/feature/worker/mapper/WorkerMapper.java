package io.github.artsobol.kurkod.feature.worker.mapper;

import io.github.artsobol.kurkod.feature.cage.mapper.CageMapper;
import io.github.artsobol.kurkod.feature.cage.entity.Cage;
import io.github.artsobol.kurkod.feature.worker.dto.response.WorkerDTO;
import io.github.artsobol.kurkod.feature.worker.entity.Worker;
import io.github.artsobol.kurkod.feature.worker.entity.WorkerCage;
import io.github.artsobol.kurkod.feature.worker.dto.request.WorkerPatchRequest;
import io.github.artsobol.kurkod.feature.worker.dto.request.WorkerPostRequest;
import io.github.artsobol.kurkod.feature.worker.dto.request.WorkerPutRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring",
nullValuePropertyMappingStrategy = org.mapstruct.NullValuePropertyMappingStrategy.IGNORE,
        uses = {CageMapper.class})
public interface WorkerMapper {

    @Mapping(target = "cages", source = "workerCages")
    WorkerDTO toDto(Worker worker);

    Worker toEntity(WorkerPostRequest workerPostRequest);

    void updateFully(@MappingTarget Worker worker, WorkerPutRequest workerPutRequest);

    void updatePartially(@MappingTarget Worker worker, WorkerPatchRequest workerPatchRequest);

    default Cage mapWorkerCageToCage(WorkerCage workerCage) {
        return workerCage.getCage();
    }
}
