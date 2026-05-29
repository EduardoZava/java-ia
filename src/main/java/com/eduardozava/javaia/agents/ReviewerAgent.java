package com.eduardozava.javaia.agents;

import com.eduardozava.javaia.model.AnalyzedSpec;
import com.eduardozava.javaia.model.ApiDesign;
import com.eduardozava.javaia.model.ServiceArchitecture;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ReviewerAgent {

    public List<String> review(AnalyzedSpec spec, ServiceArchitecture architecture, ApiDesign apiDesign) {
        List<String> notes = new ArrayList<>();
        if (apiDesign.artifacts().isEmpty()) {
            notes.add("Nenhum artefato de API foi gerado.");
        }
        notes.add("Pacote base definido: " + architecture.basePackage());
        notes.add("Entidades previstas: " + spec.entities().size());
        notes.add("Resumo da etapa de API: " + apiDesign.summary());
        return notes;
    }
}
