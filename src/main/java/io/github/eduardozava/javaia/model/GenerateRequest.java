package io.github.eduardozava.javaia.model;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

import java.util.List;

public record GenerateRequest(
        @NotBlank(message = "serviceName é obrigatório") String serviceName,
        String description,
        @Valid List<EndpointRequest> endpoints
) {
}
