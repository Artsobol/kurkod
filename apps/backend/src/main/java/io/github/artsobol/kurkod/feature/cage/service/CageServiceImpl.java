package io.github.artsobol.kurkod.feature.cage.service;

import io.github.artsobol.kurkod.exception.http.DataExistException;
import io.github.artsobol.kurkod.exception.http.NotFoundException;
import io.github.artsobol.kurkod.feature.cage.mapper.CageMapper;
import io.github.artsobol.kurkod.feature.cage.dto.response.CageResponse;
import io.github.artsobol.kurkod.feature.cage.entity.Cage;
import io.github.artsobol.kurkod.feature.cage.dto.request.CageUpdateRequest;
import io.github.artsobol.kurkod.feature.cage.dto.request.CageCreateRequest;
import io.github.artsobol.kurkod.feature.cage.repository.CageRepository;
import io.github.artsobol.kurkod.feature.cage.service.CageService;
import io.github.artsobol.kurkod.feature.rows.entity.Rows;
import io.github.artsobol.kurkod.feature.rows.repository.RowsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static io.github.artsobol.kurkod.infrastructure.util.VersionUtils.checkVersion;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CageServiceImpl implements CageService {

    private final CageRepository cageRepository;
    private final RowsRepository rowsRepository;
    private final CageMapper cageMapper;



    @Override
    public CageResponse find(Long rowId, Integer cageNumber) {
        return cageMapper.toResponse(findCageByRowIdAndCageNumber(rowId, cageNumber));
    }

    @Override
    public List<CageResponse> findAll(Long rowId) {

        if (!rowsRepository.existsById(rowId)) {
            throw new NotFoundException("row.not.found", rowId);
        }

        return cageRepository.findAllByRow_IdAndIsActiveTrueOrderByCageNumberAsc(rowId).stream()
                             .map(cageMapper::toResponse)
                             .toList();
    }

    @Override
    @Transactional
    @PreAuthorize("hasAnyAuthority('DIRECTOR', 'SUPER_ADMIN')")
    public CageResponse create(Long rowId, CageCreateRequest cageCreateRequest) {
        ensureNotExists(rowId, cageCreateRequest.getCageNumber());
        Cage cage = cageMapper.toEntity(cageCreateRequest);
        Rows rows = rowsRepository.findById(rowId).orElseThrow(
                () -> new NotFoundException("row.not.found", rowId)
        );
        cage.setRow(rows);
        cageRepository.save(cage);
        return cageMapper.toResponse(cage);
    }
    @Override
    @Transactional
    @PreAuthorize("hasAnyAuthority('DIRECTOR', 'SUPER_ADMIN')")
    public CageResponse update(Long rowId, Integer cageNumber, CageUpdateRequest cageUpdateRequest, Long version) {
        ensureExists(rowId, cageNumber);
        Integer newCageNumber = cageUpdateRequest.getCageNumber();
        if (newCageNumber != null && !newCageNumber.equals(cageNumber)) {
            ensureNotExists(rowId, cageUpdateRequest.getCageNumber());
        }

        Cage cage = findCageByRowIdAndCageNumber(rowId, cageNumber);
        checkVersion(cage.getVersion(), version);
        cageMapper.update(cage, cageUpdateRequest);
        return cageMapper.toResponse(cageRepository.save(cage));
    }

    @Override
    @Transactional
    @PreAuthorize("hasAnyAuthority('DIRECTOR', 'SUPER_ADMIN')")
    public void delete(Long rowId, Integer cageNumber, Long version) {
        Cage cage = findCageByRowIdAndCageNumber(rowId, cageNumber);
        checkVersion(cage.getVersion(), version);
        cage.setActive(false);
        cageRepository.save(cage);
    }

    protected Cage findCageByRowIdAndCageNumber(Long rowId, Integer cageNumber){
        return cageRepository.findByRow_IdAndCageNumberAndIsActiveTrue(rowId, cageNumber).orElseThrow(
                () -> new NotFoundException("cage.not.found.by.keys", rowId, cageNumber)
                                                                                                     );
    }

    protected void ensureExists(Long rowId, Integer cageNumber){
        if(!existsByRowIdAndCageNumber(rowId, cageNumber)){
            throw new NotFoundException("cage.not.found.by.keys", rowId, cageNumber);
        }
    }

    protected void ensureNotExists(Long rowId, Integer cageNumber){
        if(existsByRowIdAndCageNumber(rowId, cageNumber)){
            throw new DataExistException("cage.already.exists", rowId, cageNumber);
        }
    }

    protected boolean existsByRowIdAndCageNumber(Long rowId, Integer cageNumber){
        return cageRepository.existsByRow_IdAndCageNumberAndIsActiveTrue(rowId, cageNumber);
    }
}
