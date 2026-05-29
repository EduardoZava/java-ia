package com.eduardozava.javaia.model;

import java.util.List;

public record AnalyzedSpec(
        String serviceName,
        String normalizedDescription,
        List<String> entities,
        List<String> endpoints,
        List<String> inferredCapabilities
) {
}
