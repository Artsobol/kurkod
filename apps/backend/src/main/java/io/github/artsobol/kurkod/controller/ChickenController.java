package io.github.artsobol.kurkod.controller;

import io.github.artsobol.kurkod.model.constants.ApiLogMessage;
import io.github.artsobol.kurkod.model.dto.chicken.ChickenDTO;
import io.github.artsobol.kurkod.model.request.chicken.ChickenRequest;
import io.github.artsobol.kurkod.model.response.IamResponse;
import io.github.artsobol.kurkod.service.ChickenService;
import io.github.artsobol.kurkod.utils.ApiUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/chickens")
@RequiredArgsConstructor
public class ChickenController {

    private final ChickenService chickenService;

    @PostMapping
    public ResponseEntity<IamResponse<ChickenDTO>> createChicken(@RequestBody ChickenRequest chickenRequest) {
        log.trace(ApiLogMessage.NAME_OF_CURRENT_METHOD.getValue(), ApiUtils.getMethodName());

        IamResponse<ChickenDTO> response = chickenService.createChicken(chickenRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<IamResponse<ChickenDTO>> getChickenById(@PathVariable(name = "id") Integer id) {
        log.trace(ApiLogMessage.NAME_OF_CURRENT_METHOD.getValue(), ApiUtils.getMethodName());

        IamResponse<ChickenDTO> response = chickenService.getById(id);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
