package io.github.artsobol.kurkod.feature.worker.mapper;

import io.github.artsobol.kurkod.feature.cage.mapper.CageMapper;
import io.github.artsobol.kurkod.feature.cage.entity.Cage;
import io.github.artsobol.kurkod.feature.worker.dto.response.WorkerDTO;
import io.github.artsobol.kurkod.feature.worker.entity.Worker;
import io.github.artsobol.kurkod.feature.worker.entity.WorkerCage;
import io.github.artsobol.kurkod.feature.worker.dto.request.WorkerUpdateRequest;
import io.github.artsobol.kurkod.feature.worker.dto.request.WorkerCreateRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring",
nullValuePropertyMappingStrategy = org.mapstruct.NullValuePropertyMappingStrategy.IGNORE,
        uses = {CageMapper.class})
public interface WorkerMapper {

    @Mapping(target = "cages", source = "workerCages")
    WorkerDTO toDto(Worker worker);

    Worker toEntity(WorkerCreateRequest workerCreateRequest);
    void updatePartially(@MappingTarget Worker worker, WorkerUpdateRequest workerUpdateRequest);

    default Cage mapWorkerCageToCage(WorkerCage workerCage) {
        return workerCage.getCage();
    }
}
