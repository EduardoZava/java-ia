package io.github.eduardozava.javaia.generator;

import io.github.eduardozava.javaia.model.GenerationArtifact;
import io.github.eduardozava.javaia.model.GenerationContext;
import io.github.eduardozava.javaia.model.GenerationResult;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class GenerationResultAssembler {

    public GenerationResult assemble(GenerationContext context) {
        List<GenerationArtifact> artifacts = context.artifacts().entrySet().stream()
                .map(entry -> new GenerationArtifact(entry.getKey(), entry.getValue()))
                .toList();

        String summary = "MVP gerou %d artefato(s) para '%s'.\nResumo da especificação: %s"
                .formatted(artifacts.size(), context.spec().serviceName(), context.metadata().getOrDefault("specSummary", "sem resumo"));

        return new GenerationResult(summary, artifacts, List.copyOf(context.warnings()));
    }
}
