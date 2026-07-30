package io.github.artsobol.kurkod.feature.report.service;

import io.github.artsobol.kurkod.feature.report.dto.response.BreedEggDiffReportDTO;
import io.github.artsobol.kurkod.feature.report.entity.BreedEggDiffReport;
import io.github.artsobol.kurkod.feature.report.repository.BreedEggDiffReportRepository;
import io.github.artsobol.kurkod.feature.report.service.BreedReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@PreAuthorize("hasAnyAuthority('DIRECTOR', 'SUPER_ADMIN')")
@Transactional(readOnly = true)
public class BreedReportServiceImpl implements BreedReportService {

    private final BreedEggDiffReportRepository repository;

    @Override
    public List<BreedEggDiffReportDTO> getEggDiff() {
        return repository.findAll()
                         .stream()
                         .map(this::toDto)
                         .toList();
    }

    private BreedEggDiffReportDTO toDto(BreedEggDiffReport entity) {
        return new BreedEggDiffReportDTO(
                entity.getBreedId(),
                entity.getBreedName(),
                entity.getBreedAvgEggs(),
                entity.getFarmAvgEggs(),
                entity.getDiffEggs()
        );
    }
}