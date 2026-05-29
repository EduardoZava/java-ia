package io.github.eduardozava.javaia.orchestrator;

import io.github.eduardozava.javaia.agents.ApiAgent;
import io.github.eduardozava.javaia.agents.ArchitectureAgent;
import io.github.eduardozava.javaia.agents.ReviewerAgent;
import io.github.eduardozava.javaia.agents.SpecAnalyzerAgent;
import io.github.eduardozava.javaia.generator.GenerationResultAssembler;
import io.github.eduardozava.javaia.model.GenerationContext;
import io.github.eduardozava.javaia.model.GenerationResult;
import io.github.eduardozava.javaia.model.MicroserviceSpec;
import org.springframework.stereotype.Service;

@Service
public class GenerationOrchestrator {

    private final SpecAnalyzerAgent specAnalyzerAgent;
    private final ArchitectureAgent architectureAgent;
    private final ApiAgent apiAgent;
    private final ReviewerAgent reviewerAgent;
    private final GenerationResultAssembler generationResultAssembler;

    public GenerationOrchestrator(
            SpecAnalyzerAgent specAnalyzerAgent,
            ArchitectureAgent architectureAgent,
            ApiAgent apiAgent,
            ReviewerAgent reviewerAgent,
            GenerationResultAssembler generationResultAssembler
    ) {
        this.specAnalyzerAgent = specAnalyzerAgent;
        this.architectureAgent = architectureAgent;
        this.apiAgent = apiAgent;
        this.reviewerAgent = reviewerAgent;
        this.generationResultAssembler = generationResultAssembler;
    }

    public GenerationResult generate(MicroserviceSpec spec) {
        GenerationContext context = new GenerationContext(spec);
        specAnalyzerAgent.process(context);
        architectureAgent.process(context);
        apiAgent.process(context);
        reviewerAgent.process(context);
        return generationResultAssembler.assemble(context);
    }
}
