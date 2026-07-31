package io.github.artsobol.kurkod.feature.workshop.service;

import io.github.artsobol.kurkod.exception.http.DataExistException;
import io.github.artsobol.kurkod.exception.http.NotFoundException;
import io.github.artsobol.kurkod.feature.workshop.mapper.WorkshopMapper;
import io.github.artsobol.kurkod.feature.workshop.dto.response.WorkshopResponse;
import io.github.artsobol.kurkod.feature.workshop.entity.Workshop;
import io.github.artsobol.kurkod.feature.workshop.dto.request.WorkshopUpdateRequest;
import io.github.artsobol.kurkod.feature.workshop.dto.request.WorkshopCreateRequest;
import io.github.artsobol.kurkod.feature.workshop.repository.WorkshopRepository;
import io.github.artsobol.kurkod.feature.workshop.service.WorkshopService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static io.github.artsobol.kurkod.infrastructure.util.VersionUtils.checkVersion;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class WorkshopServiceImpl implements WorkshopService {

    private final WorkshopRepository workshopRepository;
    private final WorkshopMapper workshopMapper;



    @Override
    public WorkshopResponse get(Long id) {
        return workshopMapper.toResponse(getWorkshopById(id));
    }

    @Override
    public List<WorkshopResponse> getAll() {
        return workshopRepository.findAllByIsActiveTrue().stream()
                .map(workshopMapper::toResponse)
                .toList();
    }

    @Override
    public Page<WorkshopResponse> getAllWithPagination(Pageable pageable) {
        return workshopRepository.findAllByIsActiveTrue(pageable)
                .map(workshopMapper::toResponse);
    }

    @Override
    @Transactional
    @PreAuthorize("hasAnyAuthority('DIRECTOR', 'SUPER_ADMIN')")
    public WorkshopResponse create(WorkshopCreateRequest request) {
        Integer workshopNumber = request.getWorkshopNumber();
        ensureNotExists(workshopNumber);

        Workshop workshop = workshopMapper.toEntity(request);
        workshopRepository.save(workshop);
        return workshopMapper.toResponse(workshop);
    }

    @Override
    @Transactional
    @PreAuthorize("hasAnyAuthority('DIRECTOR', 'SUPER_ADMIN')")
    public WorkshopResponse update(Long id, WorkshopUpdateRequest request, Long version) {
        Workshop workshop = getWorkshopById(id);
        checkVersion(workshop.getVersion(), version);
        Integer newWorkshopNumber = request.getWorkshopNumber();
        if (newWorkshopNumber != null && !newWorkshopNumber.equals(workshop.getWorkshopNumber())) {
            ensureNotExists(newWorkshopNumber);
        }

        workshopMapper.update(workshop, request);
        workshopRepository.save(workshop);

        return workshopMapper.toResponse(workshop);
    }
    @Override
    @Transactional
    @PreAuthorize("hasAnyAuthority('DIRECTOR', 'SUPER_ADMIN')")
    public void delete(Long id, Long version) {
        Workshop workshop = getWorkshopById(id);
        checkVersion(workshop.getVersion(), version);
        workshop.setActive(false);
    }

    protected Workshop getWorkshopById(Long id) {
        return workshopRepository.findWorkshopByIdAndIsActiveTrue(id).orElseThrow(
                () -> new NotFoundException("workshop.not.found", id)
        );
    }

    protected void ensureNotExists(Integer id) {
        if (existsById(id)){
            throw new DataExistException("workshop.already.exists", id);
        }
    }

    protected boolean existsById(Integer id){
        return workshopRepository.existsByWorkshopNumberAndIsActiveTrue(id);
    }
}
