package com.eduardozava.javaia.model;

import jakarta.validation.constraints.NotBlank;

import java.util.List;

public record MicroserviceSpecRequest(
        @NotBlank(message = "serviceName é obrigatório") String serviceName,
        @NotBlank(message = "description é obrigatório") String description,
        List<String> entities,
        List<String> endpoints
) {
}
