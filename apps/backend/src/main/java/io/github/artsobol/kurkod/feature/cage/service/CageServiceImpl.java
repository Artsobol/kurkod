package io.github.artsobol.kurkod.feature.cage.service;

import io.github.artsobol.kurkod.infrastructure.constants.ApiLogMessage;
import io.github.artsobol.kurkod.exception.http.DataExistException;
import io.github.artsobol.kurkod.exception.http.NotFoundException;
import io.github.artsobol.kurkod.infrastructure.logging.LogHelper;
import io.github.artsobol.kurkod.infrastructure.security.facade.SecurityContextFacade;
import io.github.artsobol.kurkod.feature.cage.error.CageError;
import io.github.artsobol.kurkod.feature.cage.mapper.CageMapper;
import io.github.artsobol.kurkod.feature.cage.dto.response.CageDTO;
import io.github.artsobol.kurkod.feature.cage.entity.Cage;
import io.github.artsobol.kurkod.feature.cage.dto.request.CageUpdateRequest;
import io.github.artsobol.kurkod.feature.cage.dto.request.CageCreateRequest;
import io.github.artsobol.kurkod.feature.cage.repository.CageRepository;
import io.github.artsobol.kurkod.feature.cage.service.CageService;
import io.github.artsobol.kurkod.feature.rows.error.RowsError;
import io.github.artsobol.kurkod.feature.rows.entity.Rows;
import io.github.artsobol.kurkod.feature.rows.repository.RowsRepository;
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
public class CageServiceImpl implements CageService {

    private final CageRepository cageRepository;
    private final RowsRepository rowsRepository;
    private final CageMapper cageMapper;
    private final SecurityContextFacade securityContextFacade;

    private String getCurrentUsername(){
        return securityContextFacade.getCurrentUsername();
    }


    @Override
    public CageDTO find(Long rowId, Integer cageNumber) {
        log.debug(ApiLogMessage.GET_ENTITY.getValue(), getCurrentUsername(), LogHelper.getEntityName(Cage.class), rowId, cageNumber);
        return cageMapper.toDto(findCageByRowIdAndCageNumber(rowId, cageNumber));
    }

    @Override
    public List<CageDTO> findAll(Long rowId) {
        log.debug(ApiLogMessage.GET_ALL_ENTITIES.getValue(), getCurrentUsername(), LogHelper.getEntityName(Cage.class));

        if (!rowsRepository.existsById(rowId)) {
            throw new NotFoundException("row.not.found", rowId);
        }

        return cageRepository.findAllByRow_IdAndIsActiveTrueOrderByCageNumberAsc(rowId).stream()
                             .map(cageMapper::toDto)
                             .toList();
    }

    @Override
    @Transactional
    @PreAuthorize("hasAnyAuthority('DIRECTOR', 'SUPER_ADMIN')")
    public CageDTO create(Long rowId, CageCreateRequest cageCreateRequest) {
        ensureNotExists(rowId, cageCreateRequest.getCageNumber());
        Cage cage = cageMapper.toEntity(cageCreateRequest);
        Rows rows = rowsRepository.findById(rowId).orElseThrow(
                () -> new NotFoundException("row.not.found", rowId)
        );
        cage.setRow(rows);
        cageRepository.save(cage);
        log.info(ApiLogMessage.CREATE_ENTITY.getValue(), getCurrentUsername(), LogHelper.getEntityName(Cage.class), rowId);
        return cageMapper.toDto(cage);
    }
    @Override
    @Transactional
    @PreAuthorize("hasAnyAuthority('DIRECTOR', 'SUPER_ADMIN')")
    public CageDTO update(Long rowId, Integer cageNumber, CageUpdateRequest cageUpdateRequest, Long version) {
        ensureExists(rowId, cageNumber);
        Integer newCageNumber = cageUpdateRequest.getCageNumber();
        if (newCageNumber != null && !newCageNumber.equals(cageNumber)) {
            ensureNotExists(rowId, cageUpdateRequest.getCageNumber());
        }

        Cage cage = findCageByRowIdAndCageNumber(rowId, cageNumber);
        checkVersion(cage.getVersion(), version);
        cageMapper.update(cage, cageUpdateRequest);
        log.info(ApiLogMessage.REPLACE_ENTITY.getValue(), getCurrentUsername(), LogHelper.getEntityName(Cage.class), rowId);
        return cageMapper.toDto(cageRepository.save(cage));
    }

    @Override
    @Transactional
    @PreAuthorize("hasAnyAuthority('DIRECTOR', 'SUPER_ADMIN')")
    public void delete(Long rowId, Integer cageNumber, Long version) {
        Cage cage = findCageByRowIdAndCageNumber(rowId, cageNumber);
        checkVersion(cage.getVersion(), version);
        cage.setActive(false);
        cageRepository.save(cage);
        log.info(ApiLogMessage.DELETE_ENTITY.getValue(), getCurrentUsername(), LogHelper.getEntityName(Cage.class), rowId);
    }

    protected Cage findCageByRowIdAndCageNumber(Long rowId, Integer cageNumber){
        return cageRepository.findByRow_IdAndCageNumberAndIsActiveTrue(rowId, cageNumber).orElseThrow(
                () -> new NotFoundException("cage.not.found.by.keys", rowId, cageNumber)
                                                                                                     );
    }

    protected void ensureExists(Long rowId, Integer cageNumber){
        if(!existsByRowIdAndCageNumber(rowId, cageNumber)){
            log.info(CageError.NOT_FOUND_BY_KEYS.format(rowId, cageNumber));
            throw new NotFoundException("cage.not.found.by.keys", rowId, cageNumber);
        }
    }

    protected void ensureNotExists(Long rowId, Integer cageNumber){
        if(existsByRowIdAndCageNumber(rowId, cageNumber)){
            log.info(CageError.ALREADY_EXISTS.format(rowId, cageNumber));
            throw new DataExistException("cage.already.exists", rowId, cageNumber);
        }
    }

    protected boolean existsByRowIdAndCageNumber(Long rowId, Integer cageNumber){
        return cageRepository.existsByRow_IdAndCageNumberAndIsActiveTrue(rowId, cageNumber);
    }
}
