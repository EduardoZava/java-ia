package io.github.eduardozava.javaia.skills;

import io.github.eduardozava.javaia.infra.DeepSeekClient;
import io.github.eduardozava.javaia.model.GenerationContext;
import io.github.eduardozava.javaia.prompt.PromptTemplateService;
import org.springframework.stereotype.Component;

@Component
public class SpecSummarySkill implements Skill {

    private final DeepSeekClient deepSeekClient;
    private final PromptTemplateService promptTemplateService;

    public SpecSummarySkill(DeepSeekClient deepSeekClient, PromptTemplateService promptTemplateService) {
        this.deepSeekClient = deepSeekClient;
        this.promptTemplateService = promptTemplateService;
    }

    @Override
    public void apply(GenerationContext context) {
        String prompt = promptTemplateService.specAnalysisPrompt(context.spec());
        var completion = deepSeekClient.complete(prompt);
        String summary = completion
                .orElse("Resumo básico (fallback MVP): gerar estrutura Spring Boot com camadas controller/service/repository.");
        if (completion.isEmpty()) {
            context.addWarning(deepSeekClient.configurationStatus());
        }
        context.putMetadata("specSummary", summary);
    }
}
