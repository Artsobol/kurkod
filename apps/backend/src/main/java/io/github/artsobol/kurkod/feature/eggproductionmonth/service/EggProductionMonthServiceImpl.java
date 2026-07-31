package io.github.artsobol.kurkod.feature.eggproductionmonth.service;

import io.github.artsobol.kurkod.exception.http.DataExistException;
import io.github.artsobol.kurkod.exception.http.NotFoundException;
import io.github.artsobol.kurkod.feature.chicken.entity.Chicken;
import io.github.artsobol.kurkod.feature.chicken.repository.ChickenRepository;
import io.github.artsobol.kurkod.feature.eggproductionmonth.mapper.EggProductionMonthMapper;
import io.github.artsobol.kurkod.feature.eggproductionmonth.dto.response.EggProductionMonthResponse;
import io.github.artsobol.kurkod.feature.eggproductionmonth.entity.EggProductionMonth;
import io.github.artsobol.kurkod.feature.eggproductionmonth.dto.request.EggProductionMonthUpdateRequest;
import io.github.artsobol.kurkod.feature.eggproductionmonth.dto.request.EggProductionMonthCreateRequest;
import io.github.artsobol.kurkod.feature.eggproductionmonth.repository.EggProductionMonthRepository;
import io.github.artsobol.kurkod.feature.eggproductionmonth.service.EggProductionMonthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static io.github.artsobol.kurkod.infrastructure.util.VersionUtils.checkVersion;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EggProductionMonthServiceImpl implements EggProductionMonthService {

    private final EggProductionMonthRepository eggProductionMonthRepository;
    private final EggProductionMonthMapper eggProductionMonthMapper;
    private final ChickenRepository chickenRepository;


    @Override
    public EggProductionMonthResponse get(Long chickenId, int month, int year) {
        return eggProductionMonthMapper.toResponse(findByIdMonthYear(chickenId, month, year));
    }

    @Override
    public List<EggProductionMonthResponse> getAllByChicken(Long chickenId) {
        return eggProductionMonthRepository.findAllByChicken_IdAndIsActiveTrue(chickenId).stream().map(
                eggProductionMonthMapper::toResponse).toList();
    }

    @Override
    public List<EggProductionMonthResponse> getAllByChickenAndYear(Long chickenId, int year) {
        return eggProductionMonthRepository.findAllByChicken_IdAndYearAndIsActiveTrue(chickenId, year).stream().map(
                eggProductionMonthMapper::toResponse).toList();
    }

    @Override
    @Transactional
    @PreAuthorize("hasAnyAuthority('DIRECTOR', 'SUPER_ADMIN')")
    public EggProductionMonthResponse create(Long chickenId, int month, int year, EggProductionMonthCreateRequest request) {
        ensureNotExistsByIdMonthYear(chickenId, month, year);
        EggProductionMonth eggProductionMonth = eggProductionMonthMapper.toEntity(request);
        Chicken chicken = chickenRepository.findById(chickenId)
                                           .orElseThrow(() -> new NotFoundException("chicken.not.found", chickenId));
        eggProductionMonth.setChicken(chicken);
        eggProductionMonth.setYear(year);
        eggProductionMonth.setMonth(month);
        eggProductionMonthRepository.save(eggProductionMonth);
        return eggProductionMonthMapper.toResponse(eggProductionMonth);
    }

    @Override
    public EggProductionMonthResponse update(
            Long chickenId,
            int month,
            int year,
            EggProductionMonthUpdateRequest request,
            Long version) {
        EggProductionMonth eggProductionMonth = findByIdMonthYear(chickenId, month, year);
        checkVersion(eggProductionMonth.getVersion(), version);
        eggProductionMonthMapper.update(eggProductionMonth, request);

        return eggProductionMonthMapper.toResponse(eggProductionMonthRepository.save(eggProductionMonth));
    }

    @Override
    public void delete(Long chickenId, int month, int year, Long version) {
        EggProductionMonth eggProductionMonth = findByIdMonthYear(chickenId, month, year);
        checkVersion(eggProductionMonth.getVersion(), version);
        eggProductionMonth.setActive(false);

        eggProductionMonthRepository.save(eggProductionMonth);
    }

    @Override
    public Long countEggsByMonthAndYear(int month, int year) {
        return eggProductionMonthRepository.countEggsByMonth(month, year);
    }

    protected EggProductionMonth findByIdMonthYear(Long chickenId, int month, int year) {
        return eggProductionMonthRepository.findByChicken_IdAndMonthAndYearAndIsActiveTrue(chickenId, month, year)
                                           .orElseThrow(() -> new NotFoundException("egg.production.not.found",
                                                                                    chickenId,
                                                                                    month,
                                                                                    year));
    }

    protected void ensureNotExistsByIdMonthYear(Long chickenId, int month, int year) {
        if (existsByIdMonthYear(chickenId, month, year)) {
            throw new DataExistException("egg.production.already.exists", chickenId, month, year);
        }
    }

    protected boolean existsByIdMonthYear(Long chickenId, int month, int year) {
        return eggProductionMonthRepository.existsByChicken_IdAndMonthAndYearAndIsActiveTrue(chickenId, month, year);
    }
}
