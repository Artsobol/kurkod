package io.github.artsobol.kurkod.feature.eggproductionmonth.service;

import io.github.artsobol.kurkod.feature.eggproductionmonth.dto.response.EggProductionMonthResponse;
import io.github.artsobol.kurkod.feature.eggproductionmonth.dto.request.EggProductionMonthUpdateRequest;
import io.github.artsobol.kurkod.feature.eggproductionmonth.dto.request.EggProductionMonthCreateRequest;

import java.util.List;

public interface EggProductionMonthService {

    EggProductionMonthResponse get(Long chickenId, int month, int year);

    List<EggProductionMonthResponse> getAllByChicken(Long chickenId);

    List<EggProductionMonthResponse> getAllByChickenAndYear(Long chickenId, int year);

    EggProductionMonthResponse create(Long chickenId, int month, int year, EggProductionMonthCreateRequest request);
    EggProductionMonthResponse update(Long chickenId, int month, int year, EggProductionMonthUpdateRequest request, Long version);

    void delete(Long chickenId, int month, int year, Long version);

    Long countEggsByMonthAndYear(int month, int year);
}
