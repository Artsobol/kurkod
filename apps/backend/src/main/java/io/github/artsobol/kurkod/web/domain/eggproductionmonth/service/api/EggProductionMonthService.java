package io.github.artsobol.kurkod.web.domain.eggproductionmonth.service.api;

import io.github.artsobol.kurkod.web.domain.eggproductionmonth.model.dto.EggProductionMonthDTO;
import io.github.artsobol.kurkod.web.domain.eggproductionmonth.model.entity.EggProductionMonth;
import io.github.artsobol.kurkod.web.domain.eggproductionmonth.model.request.EggProductionMonthPatchRequest;
import io.github.artsobol.kurkod.web.domain.eggproductionmonth.model.request.EggProductionMonthPostRequest;
import io.github.artsobol.kurkod.web.domain.eggproductionmonth.model.request.EggProductionMonthPutRequest;

import java.util.List;

public interface EggProductionMonthService {

    EggProductionMonthDTO get(int chickenId, int month, int year);

    List<EggProductionMonthDTO> getAllByChicken(int chickenId);

    List<EggProductionMonthDTO> getAllByChickenAndYear(int chickenId, int year);

    EggProductionMonthDTO create(int chickenId, int month, int year, EggProductionMonthPostRequest eggProductionMonthPostRequest);

    EggProductionMonthDTO replace(int chickenId, int month, int year, EggProductionMonthPutRequest eggProductionMonthPutRequest);

    EggProductionMonthDTO update(int chickenId, int month, int year, EggProductionMonthPatchRequest eggProductionMonthPatchRequest);

    void delete(int chickenId, int month, int year);
}
