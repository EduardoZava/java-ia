package io.github.eduardozava.javaia.orchestrator;

import io.github.eduardozava.javaia.agents.ApiAgent;
import io.github.eduardozava.javaia.agents.ArchitectureAgent;
import io.github.eduardozava.javaia.agents.ReviewerAgent;
import io.github.eduardozava.javaia.agents.SpecAnalyzerAgent;
import io.github.eduardozava.javaia.generator.GenerationResultAssembler;
import io.github.eduardozava.javaia.infra.DeepSeekClient;
import io.github.eduardozava.javaia.model.EndpointSpec;
import io.github.eduardozava.javaia.model.GenerationResult;
import io.github.eduardozava.javaia.model.MicroserviceSpec;
import io.github.eduardozava.javaia.prompt.PromptTemplateService;
import io.github.eduardozava.javaia.skills.ApiSkeletonSkill;
import io.github.eduardozava.javaia.skills.SpecSummarySkill;
import io.github.eduardozava.javaia.skills.SpringStructureSkill;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GenerationOrchestratorTest {

    @Test
    void shouldGenerateArtifactsWithFallbackWhenDeepSeekIsUnavailable() {
        DeepSeekClient unavailableClient = new DeepSeekClient() {
            @Override
            public Optional<String> complete(String prompt) {
                return Optional.empty();
            }

            @Override
            public String configurationStatus() {
                return "DeepSeek desabilitado para teste";
            }
        };

        GenerationOrchestrator orchestrator = new GenerationOrchestrator(
                new SpecAnalyzerAgent(new SpecSummarySkill(unavailableClient, new PromptTemplateService())),
                new ArchitectureAgent(new SpringStructureSkill()),
                new ApiAgent(new ApiSkeletonSkill()),
                new ReviewerAgent(),
                new GenerationResultAssembler());

        GenerationResult result = orchestrator.generate(new MicroserviceSpec(
                "order-service",
                "Gerar CRUD de pedidos",
                List.of(new EndpointSpec("GET", "/orders", "Lista pedidos"))
        ));

        assertFalse(result.artifacts().isEmpty());
        assertTrue(result.summary().contains("order-service"));
        assertTrue(result.warnings().contains("DeepSeek desabilitado para teste"));
    }
}
