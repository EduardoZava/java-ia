package io.github.eduardozava.javaia.model;

import jakarta.validation.constraints.NotBlank;

public record EndpointRequest(
        @NotBlank(message = "method é obrigatório") String method,
        @NotBlank(message = "path é obrigatório") String path,
        String description
) {
}
