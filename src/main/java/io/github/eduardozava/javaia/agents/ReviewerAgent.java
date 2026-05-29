package io.github.eduardozava.javaia.agents;

import io.github.eduardozava.javaia.model.GenerationContext;
import org.springframework.stereotype.Component;

@Component
public class ReviewerAgent implements Agent {

    @Override
    public void process(GenerationContext context) {
        if (!context.metadata().containsKey("specSummary")) {
            context.addWarning("SpecAnalyzerAgent não gerou sumário de especificação.");
        }
        if (context.artifacts().isEmpty()) {
            context.addWarning("Nenhum artefato foi gerado pelos agentes anteriores.");
        }
    }
}
