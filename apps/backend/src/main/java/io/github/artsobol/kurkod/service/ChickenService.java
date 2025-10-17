package io.github.artsobol.kurkod.service;

import io.github.artsobol.kurkod.model.dto.chicken.ChickenDTO;
import io.github.artsobol.kurkod.model.request.chicken.ChickenPatchRequest;
import io.github.artsobol.kurkod.model.request.chicken.ChickenPutRequest;
import io.github.artsobol.kurkod.model.request.chicken.ChickenPostRequest;
import io.github.artsobol.kurkod.model.response.IamResponse;

import java.util.List;

public interface ChickenService {

    ChickenDTO create(ChickenPostRequest chickenPostRequest);

    ChickenDTO get(Integer id);

    List<ChickenDTO> getAll();

    void delete(Integer id);

    ChickenDTO replace(Integer id, ChickenPutRequest chickenPutRequest);

    ChickenDTO update(Integer id, ChickenPatchRequest chickenPatchRequest);
}
