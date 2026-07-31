package io.github.artsobol.kurkod.feature.eggproductionmonth.service;

import io.github.artsobol.kurkod.feature.eggproductionmonth.dto.response.EggProductionMonthDTO;
import io.github.artsobol.kurkod.feature.eggproductionmonth.dto.request.EggProductionMonthUpdateRequest;
import io.github.artsobol.kurkod.feature.eggproductionmonth.dto.request.EggProductionMonthCreateRequest;

import java.util.List;

public interface EggProductionMonthService {

    EggProductionMonthDTO get(Long chickenId, int month, int year);

    List<EggProductionMonthDTO> getAllByChicken(Long chickenId);

    List<EggProductionMonthDTO> getAllByChickenAndYear(Long chickenId, int year);

    EggProductionMonthDTO create(Long chickenId, int month, int year, EggProductionMonthCreateRequest request);
    EggProductionMonthDTO update(Long chickenId, int month, int year, EggProductionMonthUpdateRequest request, Long version);

    void delete(Long chickenId, int month, int year, Long version);

    Long countEggsByMonthAndYear(int month, int year);
}
