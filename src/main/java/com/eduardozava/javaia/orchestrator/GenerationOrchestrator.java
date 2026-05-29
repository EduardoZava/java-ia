package com.eduardozava.javaia.orchestrator;

import com.eduardozava.javaia.agents.ApiAgent;
import com.eduardozava.javaia.agents.ArchitectureAgent;
import com.eduardozava.javaia.agents.ReviewerAgent;
import com.eduardozava.javaia.agents.SpecAnalyzerAgent;
import com.eduardozava.javaia.generator.ProjectStructureGenerator;
import com.eduardozava.javaia.model.GenerationResult;
import com.eduardozava.javaia.model.MicroserviceSpecRequest;
import org.springframework.stereotype.Service;

import java.util.StringJoiner;

@Service
public class GenerationOrchestrator {

    private final SpecAnalyzerAgent specAnalyzerAgent;
    private final ArchitectureAgent architectureAgent;
    private final ApiAgent apiAgent;
    private final ReviewerAgent reviewerAgent;
    private final ProjectStructureGenerator projectStructureGenerator;

    public GenerationOrchestrator(
            SpecAnalyzerAgent specAnalyzerAgent,
            ArchitectureAgent architectureAgent,
            ApiAgent apiAgent,
            ReviewerAgent reviewerAgent,
            ProjectStructureGenerator projectStructureGenerator
    ) {
        this.specAnalyzerAgent = specAnalyzerAgent;
        this.architectureAgent = architectureAgent;
        this.apiAgent = apiAgent;
        this.reviewerAgent = reviewerAgent;
        this.projectStructureGenerator = projectStructureGenerator;
    }

    public GenerationResult generate(MicroserviceSpecRequest request) {
        var analyzedSpec = specAnalyzerAgent.analyze(request);
        var architecture = architectureAgent.design(analyzedSpec);
        var apiDesign = apiAgent.generate(analyzedSpec, architecture);
        var reviewNotes = reviewerAgent.review(analyzedSpec, architecture, apiDesign);
        var artifacts = projectStructureGenerator.generate(analyzedSpec, architecture, apiDesign);

        StringJoiner output = new StringJoiner("\n");
        output.add("Geração concluída para serviço: " + request.serviceName());
        output.add("Artefatos: " + artifacts.size());
        reviewNotes.forEach(output::add);

        return new GenerationResult(request.serviceName(), artifacts, reviewNotes, output.toString());
    }
}
