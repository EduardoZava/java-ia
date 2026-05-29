package com.eduardozava.javaia.prompt;

import com.eduardozava.javaia.model.AnalyzedSpec;
import com.eduardozava.javaia.model.ServiceArchitecture;
import org.springframework.stereotype.Component;

@Component
public class PromptTemplateService {

    public String buildApiPrompt(AnalyzedSpec spec, ServiceArchitecture architecture) {
        return "Você é um agente de API Spring. Gere um esboço de controller para o serviço '" + spec.serviceName() +
                "' usando o pacote base '" + architecture.basePackage() + "' e considere: " + spec.normalizedDescription();
    }
}
