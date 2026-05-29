package com.eduardozava.javaia.model;

import java.util.List;

public record GenerationResult(
        String serviceName,
        List<GeneratedArtifact> artifacts,
        List<String> reviewNotes,
        String consolidatedOutput
) {
}
