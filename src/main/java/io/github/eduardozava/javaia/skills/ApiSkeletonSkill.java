package io.github.eduardozava.javaia.skills;

import io.github.eduardozava.javaia.model.EndpointSpec;
import io.github.eduardozava.javaia.model.GenerationContext;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ApiSkeletonSkill implements Skill {

    @Override
    public void apply(GenerationContext context) {
        List<EndpointSpec> endpoints = context.spec().endpoints() == null ? List.of() : context.spec().endpoints();
        StringBuilder methods = new StringBuilder();

        for (EndpointSpec endpoint : endpoints) {
            methods.append("    // ").append(nullSafe(endpoint.description())).append("\n")
                    .append("    ").append(mappingAnnotation(endpoint)).append("\n")
                    .append("    public String ")
                    .append(buildMethodName(endpoint))
                    .append("() { return \"TODO\"; }\n\n");
        }

        if (methods.isEmpty()) {
            methods.append("    @GetMapping(\"/health\")\n");
            methods.append("    public String health() { return \"ok\"; }\n");
        }

        String pkg = sanitizeName(context.spec().serviceName());
        context.addArtifact("output/%s/src/main/java/com/example/%s/api/GeneratedApi.java".formatted(context.spec().serviceName(), pkg), """
                package com.example.%s.api;

                import org.springframework.web.bind.annotation.RequestMapping;
                import org.springframework.web.bind.annotation.RestController;
                import org.springframework.web.bind.annotation.GetMapping;
                import org.springframework.web.bind.annotation.PostMapping;
                import org.springframework.web.bind.annotation.PutMapping;
                import org.springframework.web.bind.annotation.DeleteMapping;

                @RestController
                @RequestMapping("/api")
                public class GeneratedApi {
                %s}
                """.formatted(pkg, methods));
    }

    private String buildMethodName(EndpointSpec endpoint) {
        String method = endpoint.method() == null ? "do" : endpoint.method().toLowerCase();
        String path = endpoint.path() == null ? "endpoint" : endpoint.path().replaceAll("[^a-zA-Z0-9]", "_");
        String candidate = (method + "_" + path).replaceAll("_+", "_").replaceAll("^_+|_+$", "");
        return candidate.isBlank() ? "generatedEndpoint" : candidate;
    }

    private String mappingAnnotation(EndpointSpec endpoint) {
        String method = endpoint.method() == null ? "GET" : endpoint.method().toUpperCase();
        String path = endpoint.path() == null || endpoint.path().isBlank() ? "/" : endpoint.path();
        return switch (method) {
            case "POST" -> "@PostMapping(\"%s\")".formatted(path);
            case "PUT" -> "@PutMapping(\"%s\")".formatted(path);
            case "DELETE" -> "@DeleteMapping(\"%s\")".formatted(path);
            default -> "@GetMapping(\"%s\")".formatted(path);
        };
    }

    private String sanitizeName(String input) {
        String sanitized = input == null ? "generated" : input.toLowerCase().replaceAll("[^a-z0-9]", "");
        return sanitized.isBlank() ? "generated" : sanitized;
    }

    private String nullSafe(String text) {
        return text == null || text.isBlank() ? "sem descrição" : text;
    }
}
