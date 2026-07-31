package io.github.artsobol.kurkod.feature.rows.service;

import io.github.artsobol.kurkod.infrastructure.constants.ApiLogMessage;
import io.github.artsobol.kurkod.exception.http.DataExistException;
import io.github.artsobol.kurkod.exception.http.NotFoundException;
import io.github.artsobol.kurkod.infrastructure.logging.LogHelper;
import io.github.artsobol.kurkod.infrastructure.security.facade.SecurityContextFacade;
import io.github.artsobol.kurkod.feature.rows.error.RowsError;
import io.github.artsobol.kurkod.feature.rows.mapper.RowsMapper;
import io.github.artsobol.kurkod.feature.rows.dto.response.RowsDTO;
import io.github.artsobol.kurkod.feature.rows.entity.Rows;
import io.github.artsobol.kurkod.feature.rows.dto.request.RowsUpdateRequest;
import io.github.artsobol.kurkod.feature.rows.dto.request.RowsCreateRequest;
import io.github.artsobol.kurkod.feature.rows.repository.RowsRepository;
import io.github.artsobol.kurkod.feature.rows.service.RowsService;
import io.github.artsobol.kurkod.feature.workshop.error.WorkshopError;
import io.github.artsobol.kurkod.feature.workshop.repository.WorkshopRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static io.github.artsobol.kurkod.infrastructure.util.VersionUtils.checkVersion;

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
    public RowsDTO find(Long workshopId, Integer rowHumber) {
        log.debug(ApiLogMessage.GET_ENTITY.getValue(), getCurrentUsername(), LogHelper.getEntityName(Rows.class), workshopId, rowHumber);
        return rowsMapper.toDto(getRowsById(workshopId, rowHumber));
    }

    @Override
    public List<RowsDTO> findAll(Long workshopId) {
        log.debug(ApiLogMessage.GET_ALL_ENTITIES.getValue(), getCurrentUsername(), LogHelper.getEntityName(Rows.class));

        if (!workshopRepository.existsById(workshopId)) {
            throw new NotFoundException("workshop.not.found", workshopId);
        }

        return rowsRepository.findAllByWorkshop_IdAndIsActiveTrue(workshopId).stream()
                .map(rowsMapper::toDto)
                .toList();
    }

    @Override
    @Transactional
    @PreAuthorize("hasAnyAuthority('DIRECTOR', 'SUPER_ADMIN')")
    public RowsDTO create(Long workshopId, RowsCreateRequest request) {
        Integer rowNumber = request.getRowNumber();
        ensureNotExists(workshopId, rowNumber);

        Rows rows = rowsMapper.toEntity(request);
        rows.setWorkshop(workshopRepository.findById(workshopId).orElseThrow(
                () -> new NotFoundException("workshop.not.found", workshopId)
        ));
        rowsRepository.save(rows);
        log.info(ApiLogMessage.CREATE_ENTITY.getValue(), getCurrentUsername(), LogHelper.getEntityName(Rows.class), workshopId);
        return rowsMapper.toDto(rows);
    }

    @Override
    @Transactional
    @PreAuthorize("hasAnyAuthority('DIRECTOR', 'SUPER_ADMIN')")
    public RowsDTO update(Long workshopId, Integer rowHumber, RowsUpdateRequest request, Long version) {
        Integer updatedRowNumber = request.getRowNumber();
        if (updatedRowNumber != null && !updatedRowNumber.equals(rowHumber)) {
            ensureNotExists(workshopId, updatedRowNumber);
        }

        Rows rows = getRowsById(workshopId, rowHumber);
        checkVersion(rows.getVersion(), version);
        rowsMapper.update(rows, request);
        rowsRepository.save(rows);
        log.info(ApiLogMessage.UPDATE_ENTITY.getValue(), getCurrentUsername(), LogHelper.getEntityName(Rows.class), workshopId);
        return rowsMapper.toDto(rows);
    }
    @Override
    @Transactional
    @PreAuthorize("hasAnyAuthority('DIRECTOR', 'SUPER_ADMIN')")
    public void delete(Long workshopId, Integer rowHumber, Long version) {
        Rows rows = getRowsById(workshopId, rowHumber);
        checkVersion(rows.getVersion(), version);
        rows.setActive(false);
        rowsRepository.save(rows);
        log.info(ApiLogMessage.DELETE_ENTITY.getValue(), getCurrentUsername(), LogHelper.getEntityName(Rows.class), workshopId);
    }

    protected Rows getRowsById(Long workshopId, Integer rowHumber) {
        return rowsRepository.findByWorkshop_IdAndRowNumberAndIsActiveTrue(workshopId, rowHumber).orElseThrow(
                () -> new NotFoundException("row.not.found.by.keys", workshopId, rowHumber)
        );
    }

    protected void ensureNotExists(Long workshopId, Integer rowNumber) {
        if (existsByWorkshopIdAndRowNumber(workshopId, rowNumber)) {
            log.info(RowsError.ALREADY_EXISTS.format(workshopId, rowNumber));
            throw new DataExistException("row.already.exists", workshopId, rowNumber);
        }
    }

    protected boolean existsByWorkshopIdAndRowNumber(Long workshopId, Integer rowNumber) {
        return rowsRepository.existsByWorkshop_IdAndRowNumberAndIsActiveTrue(workshopId, rowNumber);
    }
}
