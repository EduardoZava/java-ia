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
                    .append("    // ").append(endpoint.method()).append(" ").append(endpoint.path()).append("\n")
                    .append("    public String ")
                    .append(buildMethodName(endpoint))
                    .append("() { return \"TODO\"; }\n\n");
        }

        if (methods.isEmpty()) {
            methods.append("    public String health() { return \"ok\"; }\n");
        }

        String pkg = sanitizeName(context.spec().serviceName());
        context.addArtifact("output/%s/src/main/java/com/example/%s/api/GeneratedApi.java".formatted(context.spec().serviceName(), pkg), """
                package com.example.%s.api;

                public class GeneratedApi {
                %s}
                """.formatted(pkg, methods));
    }

    private String buildMethodName(EndpointSpec endpoint) {
        String method = endpoint.method() == null ? "do" : endpoint.method().toLowerCase();
        String path = endpoint.path() == null ? "endpoint" : endpoint.path().replaceAll("[^a-zA-Z0-9]", "_");
        return (method + "_" + path).replaceAll("_+", "_");
    }

    private String sanitizeName(String input) {
        String sanitized = input == null ? "generated" : input.toLowerCase().replaceAll("[^a-z0-9]", "");
        return sanitized.isBlank() ? "generated" : sanitized;
    }

    private String nullSafe(String text) {
        return text == null || text.isBlank() ? "sem descrição" : text;
    }
}
