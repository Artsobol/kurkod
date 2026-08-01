package io.github.artsobol.kurkod.feature.employmentcontract.web;

import io.github.artsobol.kurkod.infrastructure.utils.EtagUtils;
import io.github.artsobol.kurkod.infrastructure.utils.LocationUtils;
import io.github.artsobol.kurkod.feature.employmentcontract.dto.response.EmploymentContractResponse;
import io.github.artsobol.kurkod.feature.employmentcontract.dto.request.EmploymentContractUpdateRequest;
import io.github.artsobol.kurkod.feature.employmentcontract.dto.request.EmploymentContractCreateRequest;
import io.github.artsobol.kurkod.feature.employmentcontract.service.EmploymentContractService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/workers/{workerId}/contract", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Employment Contract", description = "Employment Contract operations")
public class EmploymentContractController {

  private final EmploymentContractService employmentContractService;

  @Operation(summary = "Get employment contract by worker ID")
  @GetMapping
  public ResponseEntity<EmploymentContractResponse> get(@PathVariable Long workerId) {

    EmploymentContractResponse response = employmentContractService.get(workerId);
    return ResponseEntity.ok().eTag(EtagUtils.toEtag(response.version())).body(response);
  }

  @Operation(summary = "Create employment contract for a worker")
  @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<EmploymentContractResponse> create(
      @PathVariable Long workerId, @RequestBody @Valid EmploymentContractCreateRequest request) {

    EmploymentContractResponse response = employmentContractService.create(workerId, request);
    return ResponseEntity.created(LocationUtils.buildLocation())
        .eTag(EtagUtils.toEtag(response.version()))
        .body(response);
  }

  @Operation(summary = "Partially update employment contract")
  @PatchMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<EmploymentContractResponse> update(
      @PathVariable Long workerId,
      @RequestBody @Valid EmploymentContractUpdateRequest request,
      @RequestHeader(value = HttpHeaders.IF_MATCH, required = false) String ifMatch) {

    long expected = EtagUtils.parseIfMatch(ifMatch);
    EmploymentContractResponse response =
        employmentContractService.update(workerId, request, expected);
    return ResponseEntity.ok().eTag(EtagUtils.toEtag(response.version())).body(response);
  }

  @Operation(summary = "Delete employment contract")
  @DeleteMapping
  public ResponseEntity<Void> delete(
      @PathVariable Long workerId,
      @RequestHeader(value = HttpHeaders.IF_MATCH, required = false) String ifMatch) {

    long expected = EtagUtils.parseIfMatch(ifMatch);
    employmentContractService.delete(workerId, expected);
    return ResponseEntity.noContent().build();
  }
}
