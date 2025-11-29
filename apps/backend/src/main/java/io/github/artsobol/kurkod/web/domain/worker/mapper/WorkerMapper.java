package io.github.artsobol.kurkod.web.domain.worker.mapper;

import io.github.artsobol.kurkod.web.domain.cage.mapper.CageMapper;
import io.github.artsobol.kurkod.web.domain.cage.model.entity.Cage;
import io.github.artsobol.kurkod.web.domain.worker.model.dto.WorkerDTO;
import io.github.artsobol.kurkod.web.domain.worker.model.entity.Worker;
import io.github.artsobol.kurkod.web.domain.worker.model.entity.WorkerCage;
import io.github.artsobol.kurkod.web.domain.worker.model.request.WorkerPatchRequest;
import io.github.artsobol.kurkod.web.domain.worker.model.request.WorkerPostRequest;
import io.github.artsobol.kurkod.web.domain.worker.model.request.WorkerPutRequest;
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
