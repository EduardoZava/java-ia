package com.eduardozava.javaia.agents;

import com.eduardozava.javaia.model.AnalyzedSpec;
import com.eduardozava.javaia.model.MicroserviceSpecRequest;
import com.eduardozava.javaia.skills.SpecSummarySkill;
import org.springframework.stereotype.Component;

@Component
public class SpecAnalyzerAgent {

    private final SpecSummarySkill specSummarySkill;

    public SpecAnalyzerAgent(SpecSummarySkill specSummarySkill) {
        this.specSummarySkill = specSummarySkill;
    }

    public AnalyzedSpec analyze(MicroserviceSpecRequest request) {
        return specSummarySkill.execute(request);
    }
}
