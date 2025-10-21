package io.github.artsobol.kurkod.web.domain.rows.service.impl;

import io.github.artsobol.kurkod.common.constants.ApiLogMessage;
import io.github.artsobol.kurkod.common.exception.DataExistException;
import io.github.artsobol.kurkod.common.exception.NotFoundException;
import io.github.artsobol.kurkod.common.logging.LogHelper;
import io.github.artsobol.kurkod.security.facade.SecurityContextFacade;
import io.github.artsobol.kurkod.web.domain.rows.error.RowsError;
import io.github.artsobol.kurkod.web.domain.rows.mapper.RowsMapper;
import io.github.artsobol.kurkod.web.domain.rows.model.dto.RowsDTO;
import io.github.artsobol.kurkod.web.domain.rows.model.entity.Rows;
import io.github.artsobol.kurkod.web.domain.rows.model.request.RowsPatchRequest;
import io.github.artsobol.kurkod.web.domain.rows.model.request.RowsPostRequest;
import io.github.artsobol.kurkod.web.domain.rows.model.request.RowsPutRequest;
import io.github.artsobol.kurkod.web.domain.rows.repository.RowsRepository;
import io.github.artsobol.kurkod.web.domain.rows.service.api.RowsService;
import io.github.artsobol.kurkod.web.domain.workshop.error.WorkshopError;
import io.github.artsobol.kurkod.web.domain.workshop.repository.WorkshopRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RowsServiceImpl implements RowsService {

    private final RowsRepository rowsRepository;
    private final RowsMapper rowsMapper;
    private final SecurityContextFacade securityContextFacade;
    private final WorkshopRepository workshopRepository;

    private String getCurrentUsername() {
        return securityContextFacade.getCurrentUsername();
    }

    @Override
    public RowsDTO find(Integer workshopId, Integer rowHumber) {
        log.debug(ApiLogMessage.GET_ENTITY.getValue(), getCurrentUsername(), LogHelper.getEntityName(Rows.class), workshopId, rowHumber);
        return rowsMapper.toDto(getRowsById(workshopId, rowHumber));
    }

    @Override
    public List<RowsDTO> findAll(Integer workshopId) {
        log.debug(ApiLogMessage.GET_ALL_ENTITIES.getValue(), getCurrentUsername(), LogHelper.getEntityName(Rows.class));
        return rowsRepository.findAllByWorkshop_IdAndIsActiveTrue(workshopId).stream()
                .map(rowsMapper::toDto)
                .toList();
    }

    @Override
    @Transactional
    @PreAuthorize("hasAnyAuthority('DIRECTOR', 'SUPER_ADMIN')")
    public RowsDTO create(Integer workshopId, RowsPostRequest rowsPostRequest) {
        Integer rowNumber = rowsPostRequest.getRowNumber();
        ensureNotExists(workshopId, rowNumber);

        Rows rows = rowsMapper.toEntity(rowsPostRequest);
        rows.setWorkshop(workshopRepository.findById(workshopId).orElseThrow(
                () -> new NotFoundException(WorkshopError.NOT_FOUND_BY_ID.format(workshopId))
        ));
        rowsRepository.save(rows);
        log.info(ApiLogMessage.CREATE_ENTITY.getValue(), getCurrentUsername(), LogHelper.getEntityName(Rows.class), workshopId);
        return rowsMapper.toDto(rows);
    }

    @Override
    @Transactional
    @PreAuthorize("hasAnyAuthority('DIRECTOR', 'SUPER_ADMIN')")
    public RowsDTO update(Integer workshopId, Integer rowHumber, RowsPatchRequest rowsPatchRequest) {
        Integer updatedRowNumber = rowsPatchRequest.getRowNumber();
        if (updatedRowNumber != null && !updatedRowNumber.equals(rowHumber)) {
            ensureNotExists(workshopId, updatedRowNumber);
        }

        Rows rows = getRowsById(workshopId, rowHumber);
        rowsMapper.update(rows, rowsPatchRequest);
        rowsRepository.save(rows);
        log.info(ApiLogMessage.UPDATE_ENTITY.getValue(), getCurrentUsername(), LogHelper.getEntityName(Rows.class), workshopId);
        return rowsMapper.toDto(rows);
    }

    @Override
    @Transactional
    @PreAuthorize("hasAnyAuthority('DIRECTOR', 'SUPER_ADMIN')")
    public RowsDTO replace(Integer workshopId, Integer rowHumber, RowsPutRequest rowsPutRequest) {
        Integer updatedRowNumber = rowsPutRequest.getRowNumber();
        if (updatedRowNumber != null && !updatedRowNumber.equals(rowHumber)) {
            ensureNotExists(workshopId, updatedRowNumber);
        }

        Rows rows = getRowsById(workshopId, rowHumber);
        rowsMapper.replace(rows, rowsPutRequest);
        rowsRepository.save(rows);
        log.info(ApiLogMessage.REPLACE_ENTITY.getValue(), getCurrentUsername(), LogHelper.getEntityName(Rows.class), workshopId);
        return rowsMapper.toDto(rows);
    }

    @Override
    @Transactional
    @PreAuthorize("hasAnyAuthority('DIRECTOR', 'SUPER_ADMIN')")
    public void delete(Integer workshopId, Integer rowHumber) {
        Rows rows = getRowsById(workshopId, rowHumber);
        rows.setIsActive(false);
        rowsRepository.save(rows);
        log.info(ApiLogMessage.DELETE_ENTITY.getValue(), getCurrentUsername(), LogHelper.getEntityName(Rows.class), workshopId);
    }

    protected Rows getRowsById(Integer workshopId, Integer rowHumber) {
        return rowsRepository.findByWorkshop_IdAndRowNumberAndIsActiveTrue(workshopId, rowHumber).orElseThrow(
                () -> new NotFoundException(RowsError.NOT_FOUND_BY_ID.format(workshopId, rowHumber))
        );
    }

    protected void ensureNotExists(Integer workshopId, Integer rowNumber) {
        if (existsByWorkshopIdAndRowNumber(workshopId, rowNumber)) {
            log.info(RowsError.ALREADY_EXISTS.format(workshopId, rowNumber));
            throw new DataExistException(RowsError.ALREADY_EXISTS.format(workshopId, rowNumber));
        }
    }

    protected boolean existsByWorkshopIdAndRowNumber(Integer workshopId, Integer rowNumber) {
        return rowsRepository.existsByWorkshop_IdAndRowNumberAndIsActiveTrue(workshopId, rowNumber);
    }
}
