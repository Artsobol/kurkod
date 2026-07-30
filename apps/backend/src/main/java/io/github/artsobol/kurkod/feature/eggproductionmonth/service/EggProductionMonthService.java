package io.github.artsobol.kurkod.feature.eggproductionmonth.service;

import io.github.artsobol.kurkod.feature.eggproductionmonth.dto.response.EggProductionMonthDTO;
import io.github.artsobol.kurkod.feature.eggproductionmonth.dto.request.EggProductionMonthPatchRequest;
import io.github.artsobol.kurkod.feature.eggproductionmonth.dto.request.EggProductionMonthPostRequest;
import io.github.artsobol.kurkod.feature.eggproductionmonth.dto.request.EggProductionMonthPutRequest;

import java.util.List;

public interface EggProductionMonthService {

    EggProductionMonthDTO get(Long chickenId, int month, int year);

    List<EggProductionMonthDTO> getAllByChicken(Long chickenId);

    List<EggProductionMonthDTO> getAllByChickenAndYear(Long chickenId, int year);

    EggProductionMonthDTO create(Long chickenId, int month, int year, EggProductionMonthPostRequest request);

    EggProductionMonthDTO replace(Long chickenId, int month, int year, EggProductionMonthPutRequest request, Long version);

    EggProductionMonthDTO update(Long chickenId, int month, int year, EggProductionMonthPatchRequest request, Long version);

    void delete(Long chickenId, int month, int year, Long version);

    Long countEggsByMonthAndYear(int month, int year);
}
