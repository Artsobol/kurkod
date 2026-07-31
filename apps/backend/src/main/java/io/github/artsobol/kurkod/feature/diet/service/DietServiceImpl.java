package io.github.artsobol.kurkod.feature.diet.service;

import static io.github.artsobol.kurkod.infrastructure.util.VersionUtils.checkVersion;

import io.github.artsobol.kurkod.exception.http.DataExistException;
import io.github.artsobol.kurkod.exception.http.NotFoundException;
import io.github.artsobol.kurkod.feature.diet.dto.request.DietCreateRequest;
import io.github.artsobol.kurkod.feature.diet.dto.request.DietUpdateRequest;
import io.github.artsobol.kurkod.feature.diet.dto.response.DietResponse;
import io.github.artsobol.kurkod.feature.diet.entity.Diet;
import io.github.artsobol.kurkod.feature.diet.mapper.DietMapper;
import io.github.artsobol.kurkod.feature.diet.repository.DietRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class DietServiceImpl implements DietService {

    private final DietRepository dietRepository;
    private final DietMapper dietMapper;


    @Override
    public DietResponse get(Long id) {
        return dietMapper.toResponse(getDietById(id));
    }

    @Override
    public List<DietResponse> getAll() {
        return dietRepository.findAllByIsActiveTrue()
                .stream()
                .map(dietMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional
    @PreAuthorize("hasAnyAuthority('DIRECTOR', 'SUPER_ADMIN')")
    public DietResponse create(DietCreateRequest request) {
        ensureNotExists(request.getCode());
        Diet diet = dietMapper.toEntity(request);
        dietRepository.save(diet);
        return dietMapper.toResponse(diet);
    }

    @Override
    @Transactional
    @PreAuthorize("hasAnyAuthority('DIRECTOR', 'SUPER_ADMIN')")
    public DietResponse update(Long id, DietUpdateRequest request, Long version) {
        Diet diet = getDietById(id);
        checkVersion(diet.getVersion(), version);
        dietMapper.update(diet, request);
        dietRepository.save(diet);
        return dietMapper.toResponse(diet);
    }
    @Override
    @Transactional
    @PreAuthorize("hasAnyAuthority('DIRECTOR', 'SUPER_ADMIN')")
    public void delete(Long id, Long version) {
        Diet diet = getDietById(id);
        checkVersion(diet.getVersion(), version);
        diet.deactivate();
        dietRepository.save(diet);
    }

    protected void ensureNotExists(String code) {
        if (existsByCode(code)){
            throw new DataExistException("diet.already.exists", code);
        }
    }

    protected boolean existsByCode(String code){
        return dietRepository.existsByCodeAndIsActiveTrue(code);
    }

    protected Diet getDietById(Long id){
        return dietRepository.findById(id).orElseThrow(() -> new NotFoundException("diet.not.found", id));
    }
}
