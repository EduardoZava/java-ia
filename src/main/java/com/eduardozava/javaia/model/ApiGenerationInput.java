package com.eduardozava.javaia.model;

public record ApiGenerationInput(
        AnalyzedSpec analyzedSpec,
        ServiceArchitecture architecture
) {
}
