package io.github.artsobol.kurkod.feature.rows.service;

import static io.github.artsobol.kurkod.infrastructure.util.VersionUtils.checkVersion;

import io.github.artsobol.kurkod.exception.http.DataExistException;
import io.github.artsobol.kurkod.exception.http.NotFoundException;
import io.github.artsobol.kurkod.feature.rows.dto.request.RowsCreateRequest;
import io.github.artsobol.kurkod.feature.rows.dto.request.RowsUpdateRequest;
import io.github.artsobol.kurkod.feature.rows.dto.response.RowsResponse;
import io.github.artsobol.kurkod.feature.rows.entity.Rows;
import io.github.artsobol.kurkod.feature.rows.mapper.RowsMapper;
import io.github.artsobol.kurkod.feature.rows.repository.RowsRepository;
import io.github.artsobol.kurkod.feature.workshop.repository.WorkshopRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RowsServiceImpl implements RowsService {

    private final RowsRepository rowsRepository;
    private final RowsMapper rowsMapper;
    private final WorkshopRepository workshopRepository;


    @Override
    public RowsResponse find(Long workshopId, Integer rowHumber) {
        return rowsMapper.toResponse(getRowsById(workshopId, rowHumber));
    }

    @Override
    public List<RowsResponse> findAll(Long workshopId) {

        if (!workshopRepository.existsById(workshopId)) {
            throw new NotFoundException("workshop.not.found", workshopId);
        }

        return rowsRepository.findAllByWorkshop_IdAndIsActiveTrue(workshopId).stream()
                .map(rowsMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional
    @PreAuthorize("hasAnyAuthority('DIRECTOR', 'SUPER_ADMIN')")
    public RowsResponse create(Long workshopId, RowsCreateRequest request) {
        Integer rowNumber = request.getRowNumber();
        ensureNotExists(workshopId, rowNumber);

        Rows rows = rowsMapper.toEntity(request);
        rows.setWorkshop(workshopRepository.findById(workshopId).orElseThrow(
                () -> new NotFoundException("workshop.not.found", workshopId)
        ));
        rowsRepository.save(rows);
        return rowsMapper.toResponse(rows);
    }

    @Override
    @Transactional
    @PreAuthorize("hasAnyAuthority('DIRECTOR', 'SUPER_ADMIN')")
    public RowsResponse update(Long workshopId, Integer rowHumber, RowsUpdateRequest request, Long version) {
        Integer updatedRowNumber = request.getRowNumber();
        if (updatedRowNumber != null && !updatedRowNumber.equals(rowHumber)) {
            ensureNotExists(workshopId, updatedRowNumber);
        }

        Rows rows = getRowsById(workshopId, rowHumber);
        checkVersion(rows.getVersion(), version);
        rowsMapper.update(rows, request);
        rowsRepository.save(rows);
        return rowsMapper.toResponse(rows);
    }
    @Override
    @Transactional
    @PreAuthorize("hasAnyAuthority('DIRECTOR', 'SUPER_ADMIN')")
    public void delete(Long workshopId, Integer rowHumber, Long version) {
        Rows rows = getRowsById(workshopId, rowHumber);
        checkVersion(rows.getVersion(), version);
        rows.deactivate();
        rowsRepository.save(rows);
    }

    protected Rows getRowsById(Long workshopId, Integer rowHumber) {
        return rowsRepository.findByWorkshop_IdAndRowNumberAndIsActiveTrue(workshopId, rowHumber).orElseThrow(
                () -> new NotFoundException("row.not.found.by.keys", workshopId, rowHumber)
        );
    }

    protected void ensureNotExists(Long workshopId, Integer rowNumber) {
        if (existsByWorkshopIdAndRowNumber(workshopId, rowNumber)) {
            throw new DataExistException("row.already.exists", workshopId, rowNumber);
        }
    }

    protected boolean existsByWorkshopIdAndRowNumber(Long workshopId, Integer rowNumber) {
        return rowsRepository.existsByWorkshop_IdAndRowNumberAndIsActiveTrue(workshopId, rowNumber);
    }
}
