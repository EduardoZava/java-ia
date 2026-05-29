package io.github.eduardozava.javaia.prompt;

import io.github.eduardozava.javaia.model.MicroserviceSpec;
import org.springframework.stereotype.Component;

@Component
public class PromptTemplateService {

    public String specAnalysisPrompt(MicroserviceSpec spec) {
        return "Analise a especificação do microserviço e resuma arquitetura e pontos críticos. " +
                "Nome: %s; Descrição: %s".formatted(spec.serviceName(), defaultText(spec.description()));
    }

    private String defaultText(String text) {
        return text == null || text.isBlank() ? "sem descrição" : text;
    }
}
