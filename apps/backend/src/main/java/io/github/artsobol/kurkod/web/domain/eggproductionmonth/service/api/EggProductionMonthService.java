package io.github.artsobol.kurkod.web.domain.eggproductionmonth.service.api;

import io.github.artsobol.kurkod.web.domain.eggproductionmonth.model.dto.EggProductionMonthDTO;
import io.github.artsobol.kurkod.web.domain.eggproductionmonth.model.request.EggProductionMonthPatchRequest;
import io.github.artsobol.kurkod.web.domain.eggproductionmonth.model.request.EggProductionMonthPostRequest;
import io.github.artsobol.kurkod.web.domain.eggproductionmonth.model.request.EggProductionMonthPutRequest;

import java.util.List;

public interface EggProductionMonthService {

    EggProductionMonthDTO get(int chickenId, int month, int year);

    List<EggProductionMonthDTO> getAllByChicken(int chickenId);

    List<EggProductionMonthDTO> getAllByChickenAndYear(int chickenId, int year);

    EggProductionMonthDTO create(int chickenId, int month, int year, EggProductionMonthPostRequest request);

    EggProductionMonthDTO replace(int chickenId, int month, int year, EggProductionMonthPutRequest request, Long version);

    EggProductionMonthDTO update(int chickenId, int month, int year, EggProductionMonthPatchRequest request, Long version);

    void delete(int chickenId, int month, int year, Long version);

    Long countEggsByMonthAndYear(int month, int year);
}
