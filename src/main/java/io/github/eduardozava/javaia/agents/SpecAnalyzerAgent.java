package io.github.eduardozava.javaia.agents;

import io.github.eduardozava.javaia.model.GenerationContext;
import io.github.eduardozava.javaia.skills.SpecSummarySkill;
import org.springframework.stereotype.Component;

@Component
public class SpecAnalyzerAgent implements Agent {

    private final SpecSummarySkill specSummarySkill;

    public SpecAnalyzerAgent(SpecSummarySkill specSummarySkill) {
        this.specSummarySkill = specSummarySkill;
    }

    @Override
    public void process(GenerationContext context) {
        specSummarySkill.apply(context);
    }
}
