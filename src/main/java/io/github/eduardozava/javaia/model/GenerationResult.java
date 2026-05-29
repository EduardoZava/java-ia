package io.github.eduardozava.javaia.model;

import java.util.List;

public record GenerationResult(String summary, List<GenerationArtifact> artifacts, List<String> warnings) {
}
