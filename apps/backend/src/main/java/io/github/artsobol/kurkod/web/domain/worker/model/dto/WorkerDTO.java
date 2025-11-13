package io.github.artsobol.kurkod.web.domain.worker.model.dto;

public record WorkerDTO(
        Integer id, String firstName, String lastName, String patronymic, Long version
) {
};