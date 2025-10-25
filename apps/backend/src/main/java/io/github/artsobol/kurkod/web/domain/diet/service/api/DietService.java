package io.github.artsobol.kurkod.web.domain.diet.service.api;

import io.github.artsobol.kurkod.web.domain.diet.model.dto.DietDTO;
import io.github.artsobol.kurkod.web.domain.diet.model.request.DietPatchRequest;
import io.github.artsobol.kurkod.web.domain.diet.model.request.DietPostRequest;
import io.github.artsobol.kurkod.web.domain.diet.model.request.DietPutRequest;

import java.util.List;

public interface DietService {

    DietDTO get(Integer id);

    List<DietDTO> getAll();

    DietDTO create(DietPostRequest dietPostRequest);

    DietDTO update(Integer id, DietPatchRequest dietPatchRequest);

    DietDTO replace(Integer id, DietPutRequest dietPutRequest);

    void delete(Integer id);
}
