package io.github.artsobol.kurkod.web.domain.eggproductionmonth.service.impl;

import io.github.artsobol.kurkod.common.constants.ApiLogMessage;
import io.github.artsobol.kurkod.common.exception.DataExistException;
import io.github.artsobol.kurkod.common.exception.NotFoundException;
import io.github.artsobol.kurkod.common.logging.LogHelper;
import io.github.artsobol.kurkod.security.facade.SecurityContextFacade;
import io.github.artsobol.kurkod.web.domain.chicken.error.ChickenError;
import io.github.artsobol.kurkod.web.domain.chicken.model.entity.Chicken;
import io.github.artsobol.kurkod.web.domain.chicken.repository.ChickenRepository;
import io.github.artsobol.kurkod.web.domain.eggproductionmonth.error.EggProductionMonthError;
import io.github.artsobol.kurkod.web.domain.eggproductionmonth.mapper.EggProductionMonthMapper;
import io.github.artsobol.kurkod.web.domain.eggproductionmonth.model.dto.EggProductionMonthDTO;
import io.github.artsobol.kurkod.web.domain.eggproductionmonth.model.entity.EggProductionMonth;
import io.github.artsobol.kurkod.web.domain.eggproductionmonth.model.request.EggProductionMonthPatchRequest;
import io.github.artsobol.kurkod.web.domain.eggproductionmonth.model.request.EggProductionMonthPostRequest;
import io.github.artsobol.kurkod.web.domain.eggproductionmonth.model.request.EggProductionMonthPutRequest;
import io.github.artsobol.kurkod.web.domain.eggproductionmonth.repository.EggProductionMonthRepository;
import io.github.artsobol.kurkod.web.domain.eggproductionmonth.service.api.EggProductionMonthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EggProductionMonthServiceImpl implements EggProductionMonthService {

    private final EggProductionMonthRepository eggProductionMonthRepository;
    private final EggProductionMonthMapper eggProductionMonthMapper;
    private final ChickenRepository chickenRepository;
    private final SecurityContextFacade securityContextFacade;

    private String getCurrentUsername(){
        return securityContextFacade.getCurrentUsername();
    }

    @Override
    public EggProductionMonthDTO get(int chickenId, int month, int year) {
        log.debug(ApiLogMessage.GET_ENTITY.getValue(), getCurrentUsername(), LogHelper.getEntityName(EggProductionMonth.class), chickenId, month, year);
        return eggProductionMonthMapper.toDto(findByIdMonthYear(chickenId, month, year));
    }

    @Override
    public List<EggProductionMonthDTO> getAllByChicken(int chickenId) {
        log.debug(ApiLogMessage.GET_ALL_ENTITIES.getValue(), getCurrentUsername(), LogHelper.getEntityName(EggProductionMonth.class));
        return eggProductionMonthRepository.findAllByChicken_IdAndIsActiveTrue(chickenId)
                .stream()
                .map(eggProductionMonthMapper::toDto)
                .toList();
    }

    @Override
    public List<EggProductionMonthDTO> getAllByChickenAndYear(int chickenId, int year) {
        log.debug(ApiLogMessage.GET_ALL_ENTITIES.getValue(), getCurrentUsername(), LogHelper.getEntityName(EggProductionMonth.class));
        return eggProductionMonthRepository.findAllByChicken_IdAndYearAndIsActiveTrue(chickenId, year)
                .stream()
                .map(eggProductionMonthMapper::toDto)
                .toList();
    }

    @Override
    @Transactional
    @PreAuthorize("hasAnyAuthority('DIRECTOR', 'SUPER_ADMIN')")
    public EggProductionMonthDTO create(int chickenId,
                                        int month,
                                        int year,
                                        EggProductionMonthPostRequest eggProductionMonthPostRequest) {
        ensureNotExistsByIdMonthYear(chickenId, month, year);
        EggProductionMonth eggProductionMonth = eggProductionMonthMapper.toEntity(eggProductionMonthPostRequest);
        Chicken chicken = chickenRepository.findById(chickenId).orElseThrow(
                () -> new NotFoundException(ChickenError.NOT_FOUND_BY_ID.format(chickenId))
        );
        eggProductionMonth.setChicken(chicken);

        eggProductionMonthRepository.save(eggProductionMonth);
        log.info(ApiLogMessage.CREATE_ENTITY.getValue(), getCurrentUsername(), LogHelper.getEntityName(EggProductionMonth.class), chickenId);
        return eggProductionMonthMapper.toDto(eggProductionMonth);
    }

    @Override
    public EggProductionMonthDTO replace(int chickenId,
                                      int month,
                                      int year,
                                      EggProductionMonthPutRequest eggProductionMonthPutRequest) {
        EggProductionMonth eggProductionMonth = findByIdMonthYear(chickenId, month, year);
        eggProductionMonthMapper.replace(eggProductionMonth, eggProductionMonthPutRequest);

        log.info(ApiLogMessage.REPLACE_ENTITY.getValue(), getCurrentUsername(), LogHelper.getEntityName(EggProductionMonth.class), chickenId);
        return  eggProductionMonthMapper.toDto(eggProductionMonthRepository.save(eggProductionMonth));
    }

    @Override
    public EggProductionMonthDTO update(int chickenId,
                                     int month,
                                     int year,
                                     EggProductionMonthPatchRequest eggProductionMonthPatchRequest) {
        EggProductionMonth eggProductionMonth = findByIdMonthYear(chickenId, month, year);
        eggProductionMonthMapper.update(eggProductionMonth, eggProductionMonthPatchRequest);

        log.info(ApiLogMessage.REPLACE_ENTITY.getValue(), getCurrentUsername(), LogHelper.getEntityName(EggProductionMonth.class), chickenId);
        return eggProductionMonthMapper.toDto(eggProductionMonthRepository.save(eggProductionMonth));
    }

    @Override
    public void delete(int chickenId, int month, int year) {
        EggProductionMonth eggProductionMonth = findByIdMonthYear(chickenId, month, year);
        eggProductionMonth.setActive(false);
        log.info(ApiLogMessage.DELETE_ENTITY.getValue(), getCurrentUsername(), LogHelper.getEntityName(EggProductionMonth.class), chickenId);
        eggProductionMonthRepository.save(eggProductionMonth);
    }

    protected EggProductionMonth findByIdMonthYear(int chickenId, int month, int year) {
        return eggProductionMonthRepository.findByChicken_IdAndMonthAndYearAndIsActiveTrue(chickenId, month, year)
                .orElseThrow(() -> new NotFoundException(EggProductionMonthError.NOT_FOUND_BY_ID.format(chickenId, month, year))
                );
    }

    protected void ensureNotExistsByIdMonthYear(int chickenId, int month, int year) {
        if(existsByIdMonthYear(chickenId, month, year)){
            log.info(EggProductionMonthError.ALREADY_EXISTS.format(chickenId, month, year));
            throw new DataExistException(EggProductionMonthError.ALREADY_EXISTS.format(chickenId, month, year));
        }
    }

    protected boolean existsByIdMonthYear(int chickenId, int month, int year) {
        return eggProductionMonthRepository.existsByChicken_IdAndMonthAndYearAndIsActiveTrue(chickenId, month, year);
    }
}
