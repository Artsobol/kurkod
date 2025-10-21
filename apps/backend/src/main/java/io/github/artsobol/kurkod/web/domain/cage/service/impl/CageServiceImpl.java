package io.github.artsobol.kurkod.web.domain.cage.service.impl;

import io.github.artsobol.kurkod.common.constants.ApiLogMessage;
import io.github.artsobol.kurkod.common.exception.DataExistException;
import io.github.artsobol.kurkod.common.exception.NotFoundException;
import io.github.artsobol.kurkod.common.logging.LogHelper;
import io.github.artsobol.kurkod.security.facade.SecurityContextFacade;
import io.github.artsobol.kurkod.web.domain.cage.error.CageError;
import io.github.artsobol.kurkod.web.domain.cage.mapper.CageMapper;
import io.github.artsobol.kurkod.web.domain.cage.model.dto.CageDTO;
import io.github.artsobol.kurkod.web.domain.cage.model.entity.Cage;
import io.github.artsobol.kurkod.web.domain.cage.model.request.CagePatchRequest;
import io.github.artsobol.kurkod.web.domain.cage.model.request.CagePostRequest;
import io.github.artsobol.kurkod.web.domain.cage.model.request.CagePutRequest;
import io.github.artsobol.kurkod.web.domain.cage.repository.CageRepository;
import io.github.artsobol.kurkod.web.domain.cage.service.api.CageService;
import io.github.artsobol.kurkod.web.domain.rows.error.RowsError;
import io.github.artsobol.kurkod.web.domain.rows.model.entity.Rows;
import io.github.artsobol.kurkod.web.domain.rows.repository.RowsRepository;
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
public class CageServiceImpl implements CageService {

    private final CageRepository cageRepository;
    private final RowsRepository rowsRepository;
    private final CageMapper cageMapper;
    private final SecurityContextFacade securityContextFacade;

    private String getCurrentUsername(){
        return securityContextFacade.getCurrentUsername();
    }


    @Override
    public CageDTO find(Integer rowId, Integer cageNumber) {
        log.debug(ApiLogMessage.GET_ENTITY.getValue(), getCurrentUsername(), LogHelper.getEntityName(Cage.class), rowId, cageNumber);
        return cageMapper.toDto(findCageByRowIdAndCageNumber(rowId, cageNumber));
    }

    @Override
    public List<CageDTO> findAll(Integer rowId) {
        log.debug(ApiLogMessage.GET_ALL_ENTITIES.getValue(), getCurrentUsername(), LogHelper.getEntityName(Cage.class));
        return cageRepository.findAllByRow_IdOrderByCageNumberAsc(rowId).stream()
                .map(cageMapper::toDto)
                .toList();
    }

    @Override
    @Transactional
    @PreAuthorize("hasAnyAuthority('DIRECTOR', 'SUPER_ADMIN')")
    public CageDTO create(Integer rowId, CagePostRequest cagePostRequest) {
        ensureNotExists(rowId, cagePostRequest.getCageNumber());
        Cage cage = cageMapper.toEntity(cagePostRequest);
        Rows rows = rowsRepository.findById(rowId).orElseThrow(
                () -> new NotFoundException(RowsError.NOT_FOUND_BY_ID.format(rowId))
        );
        cage.setRow(rows);
        cageRepository.save(cage);
        log.info(ApiLogMessage.CREATE_ENTITY.getValue(), getCurrentUsername(), LogHelper.getEntityName(Cage.class), rowId);
        return cageMapper.toDto(cage);
    }

    @Override
    @Transactional
    @PreAuthorize("hasAnyAuthority('DIRECTOR', 'SUPER_ADMIN')")
    public CageDTO replace(Integer rowId, Integer cageNumber, CagePutRequest cagePutRequest) {
        ensureExists(rowId, cageNumber);
        Integer newCageNumber = cagePutRequest.getCageNumber();
        if (!newCageNumber.equals(cageNumber)) {
            ensureNotExists(rowId, newCageNumber);
        }

        Cage cage = findCageByRowIdAndCageNumber(rowId, cageNumber);
        cageMapper.replace(cage, cagePutRequest);
        log.info(ApiLogMessage.REPLACE_ENTITY.getValue(), getCurrentUsername(), LogHelper.getEntityName(Cage.class), rowId);
        return cageMapper.toDto(cageRepository.save(cage));
    }

    @Override
    @Transactional
    @PreAuthorize("hasAnyAuthority('DIRECTOR', 'SUPER_ADMIN')")
    public CageDTO update(Integer rowId, Integer cageNumber, CagePatchRequest cagePatchRequest) {
        ensureExists(rowId, cageNumber);
        Integer newCageNumber = cagePatchRequest.getCageNumber();
        if (newCageNumber != null && !newCageNumber.equals(cageNumber)) {
            ensureNotExists(rowId, cagePatchRequest.getCageNumber());
        }

        Cage cage = findCageByRowIdAndCageNumber(rowId, cageNumber);
        cageMapper.update(cage, cagePatchRequest);
        log.info(ApiLogMessage.REPLACE_ENTITY.getValue(), getCurrentUsername(), LogHelper.getEntityName(Cage.class), rowId);
        return cageMapper.toDto(cageRepository.save(cage));
    }

    @Override
    @Transactional
    @PreAuthorize("hasAnyAuthority('DIRECTOR', 'SUPER_ADMIN')")
    public void delete(Integer rowId, Integer cageNumber) {
        Cage cage = findCageByRowIdAndCageNumber(rowId, cageNumber);
        cage.setIsActive(false);
        cageRepository.save(cage);
        log.info(ApiLogMessage.DELETE_ENTITY.getValue(), getCurrentUsername(), LogHelper.getEntityName(Cage.class), rowId);
    }

    protected Cage findCageByRowIdAndCageNumber(Integer rowId, Integer cageNumber){
        return cageRepository.findByRow_IdAndCageNumber(rowId, cageNumber).orElseThrow(
                () -> new NotFoundException(CageError.NOT_FOUND_BY_ID.format(rowId, cageNumber))
        );
    }

    protected void ensureExists(Integer rowId, Integer cageNumber){
        if(!existsByRowIdAndCageNumber(rowId, cageNumber)){
            log.info(CageError.NOT_FOUND_BY_ID.format(rowId, cageNumber));
            throw new NotFoundException(CageError.NOT_FOUND_BY_ID.format(rowId, cageNumber));
        }
    }

    protected void ensureNotExists(Integer rowId, Integer cageNumber){
        if(existsByRowIdAndCageNumber(rowId, cageNumber)){
            log.info(CageError.ALREADY_EXISTS.format(rowId, cageNumber));
            throw new DataExistException(CageError.ALREADY_EXISTS.format(rowId, cageNumber));
        }
    }

    protected boolean existsByRowIdAndCageNumber(Integer rowId, Integer cageNumber){
        return cageRepository.existsByRow_IdAndCageNumber(rowId, cageNumber);
    }
}
