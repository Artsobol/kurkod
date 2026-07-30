package io.github.artsobol.kurkod.feature.eggproductionmonth.service;

import io.github.artsobol.kurkod.infrastructure.constants.ApiLogMessage;
import io.github.artsobol.kurkod.exception.http.DataExistException;
import io.github.artsobol.kurkod.exception.http.NotFoundException;
import io.github.artsobol.kurkod.infrastructure.logging.LogHelper;
import io.github.artsobol.kurkod.infrastructure.security.facade.SecurityContextFacade;
import io.github.artsobol.kurkod.feature.chicken.error.ChickenError;
import io.github.artsobol.kurkod.feature.chicken.entity.Chicken;
import io.github.artsobol.kurkod.feature.chicken.repository.ChickenRepository;
import io.github.artsobol.kurkod.feature.eggproductionmonth.error.EggProductionMonthError;
import io.github.artsobol.kurkod.feature.eggproductionmonth.mapper.EggProductionMonthMapper;
import io.github.artsobol.kurkod.feature.eggproductionmonth.dto.response.EggProductionMonthDTO;
import io.github.artsobol.kurkod.feature.eggproductionmonth.entity.EggProductionMonth;
import io.github.artsobol.kurkod.feature.eggproductionmonth.dto.request.EggProductionMonthPatchRequest;
import io.github.artsobol.kurkod.feature.eggproductionmonth.dto.request.EggProductionMonthPostRequest;
import io.github.artsobol.kurkod.feature.eggproductionmonth.dto.request.EggProductionMonthPutRequest;
import io.github.artsobol.kurkod.feature.eggproductionmonth.repository.EggProductionMonthRepository;
import io.github.artsobol.kurkod.feature.eggproductionmonth.service.EggProductionMonthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static io.github.artsobol.kurkod.infrastructure.util.VersionUtils.checkVersion;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EggProductionMonthServiceImpl implements EggProductionMonthService {

    private final EggProductionMonthRepository eggProductionMonthRepository;
    private final EggProductionMonthMapper eggProductionMonthMapper;
    private final ChickenRepository chickenRepository;
    private final SecurityContextFacade securityContextFacade;

    private String getCurrentUsername() {
        return securityContextFacade.getCurrentUsername();
    }

    @Override
    public EggProductionMonthDTO get(Long chickenId, int month, int year) {
        log.debug(ApiLogMessage.GET_ENTITY.getValue(),
                  getCurrentUsername(),
                  LogHelper.getEntityName(EggProductionMonth.class),
                  chickenId,
                  month,
                  year);
        return eggProductionMonthMapper.toDto(findByIdMonthYear(chickenId, month, year));
    }

    @Override
    public List<EggProductionMonthDTO> getAllByChicken(Long chickenId) {
        log.debug(ApiLogMessage.GET_ALL_ENTITIES.getValue(),
                  getCurrentUsername(),
                  LogHelper.getEntityName(EggProductionMonth.class));
        return eggProductionMonthRepository.findAllByChicken_IdAndIsActiveTrue(chickenId).stream().map(
                eggProductionMonthMapper::toDto).toList();
    }

    @Override
    public List<EggProductionMonthDTO> getAllByChickenAndYear(Long chickenId, int year) {
        log.debug(ApiLogMessage.GET_ALL_ENTITIES.getValue(),
                  getCurrentUsername(),
                  LogHelper.getEntityName(EggProductionMonth.class));
        return eggProductionMonthRepository.findAllByChicken_IdAndYearAndIsActiveTrue(chickenId, year).stream().map(
                eggProductionMonthMapper::toDto).toList();
    }

    @Override
    @Transactional
    @PreAuthorize("hasAnyAuthority('DIRECTOR', 'SUPER_ADMIN')")
    public EggProductionMonthDTO create(Long chickenId, int month, int year, EggProductionMonthPostRequest request) {
        ensureNotExistsByIdMonthYear(chickenId, month, year);
        EggProductionMonth eggProductionMonth = eggProductionMonthMapper.toEntity(request);
        Chicken chicken = chickenRepository.findById(chickenId)
                                           .orElseThrow(() -> new NotFoundException(ChickenError.NOT_FOUND_BY_ID, chickenId));
        eggProductionMonth.setChicken(chicken);
        eggProductionMonth.setYear(year);
        eggProductionMonth.setMonth(month);
        eggProductionMonthRepository.save(eggProductionMonth);
        log.info(ApiLogMessage.CREATE_ENTITY.getValue(),
                 getCurrentUsername(),
                 LogHelper.getEntityName(EggProductionMonth.class),
                 chickenId);
        return eggProductionMonthMapper.toDto(eggProductionMonth);
    }

    @Override
    public EggProductionMonthDTO replace(
            Long chickenId,
            int month,
            int year,
            EggProductionMonthPutRequest request,
            Long version) {
        EggProductionMonth eggProductionMonth = findByIdMonthYear(chickenId, month, year);
        checkVersion(eggProductionMonth.getVersion(), version);
        eggProductionMonthMapper.replace(eggProductionMonth, request);

        log.info(ApiLogMessage.REPLACE_ENTITY.getValue(),
                 getCurrentUsername(),
                 LogHelper.getEntityName(EggProductionMonth.class),
                 chickenId);
        return eggProductionMonthMapper.toDto(eggProductionMonthRepository.save(eggProductionMonth));
    }

    @Override
    public EggProductionMonthDTO update(
            Long chickenId,
            int month,
            int year,
            EggProductionMonthPatchRequest request,
            Long version) {
        EggProductionMonth eggProductionMonth = findByIdMonthYear(chickenId, month, year);
        checkVersion(eggProductionMonth.getVersion(), version);
        eggProductionMonthMapper.update(eggProductionMonth, request);

        log.info(ApiLogMessage.REPLACE_ENTITY.getValue(),
                 getCurrentUsername(),
                 LogHelper.getEntityName(EggProductionMonth.class),
                 chickenId);
        return eggProductionMonthMapper.toDto(eggProductionMonthRepository.save(eggProductionMonth));
    }

    @Override
    public void delete(Long chickenId, int month, int year, Long version) {
        EggProductionMonth eggProductionMonth = findByIdMonthYear(chickenId, month, year);
        checkVersion(eggProductionMonth.getVersion(), version);
        eggProductionMonth.setActive(false);

        log.info(ApiLogMessage.DELETE_ENTITY.getValue(),
                 getCurrentUsername(),
                 LogHelper.getEntityName(EggProductionMonth.class),
                 chickenId);
        eggProductionMonthRepository.save(eggProductionMonth);
    }

    @Override
    public Long countEggsByMonthAndYear(int month, int year) {
        return eggProductionMonthRepository.countEggsByMonth(month, year);
    }

    protected EggProductionMonth findByIdMonthYear(Long chickenId, int month, int year) {
        return eggProductionMonthRepository.findByChicken_IdAndMonthAndYearAndIsActiveTrue(chickenId, month, year)
                                           .orElseThrow(() -> new NotFoundException(EggProductionMonthError.NOT_FOUND_BY_KEYS,
                                                                                    chickenId,
                                                                                    month,
                                                                                    year));
    }

    protected void ensureNotExistsByIdMonthYear(Long chickenId, int month, int year) {
        if (existsByIdMonthYear(chickenId, month, year)) {
            log.info(EggProductionMonthError.ALREADY_EXISTS.format(chickenId, month, year));
            throw new DataExistException(EggProductionMonthError.ALREADY_EXISTS, chickenId, month, year);
        }
    }

    protected boolean existsByIdMonthYear(Long chickenId, int month, int year) {
        return eggProductionMonthRepository.existsByChicken_IdAndMonthAndYearAndIsActiveTrue(chickenId, month, year);
    }
}
