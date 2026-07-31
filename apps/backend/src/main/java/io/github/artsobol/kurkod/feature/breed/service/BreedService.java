package io.github.artsobol.kurkod.feature.breed.service;

import io.github.artsobol.kurkod.feature.breed.dto.response.BreedDTO;
import io.github.artsobol.kurkod.feature.breed.dto.request.BreedUpdateRequest;
import io.github.artsobol.kurkod.feature.breed.dto.request.BreedCreateRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

public interface BreedService {

    BreedDTO create(BreedCreateRequest breedCreateRequest);

    BreedDTO get(Long id);

    List<BreedDTO> getAll();

    Page<BreedDTO> getPage(Pageable pageable);
    BreedDTO update(Long id, BreedUpdateRequest breedUpdateRequest, Long version);

    void delete(Long id, Long version);
}
