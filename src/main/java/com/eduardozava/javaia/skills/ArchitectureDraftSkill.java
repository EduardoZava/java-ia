package com.eduardozava.javaia.skills;

import com.eduardozava.javaia.model.AnalyzedSpec;
import com.eduardozava.javaia.model.ServiceArchitecture;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ArchitectureDraftSkill implements Skill<AnalyzedSpec, ServiceArchitecture> {

    @Override
    public ServiceArchitecture execute(AnalyzedSpec input) {
        String normalized = input.serviceName().toLowerCase().replaceAll("[^a-z0-9]", "");
        String basePackage = "com.generated." + (normalized.isBlank() ? "service" : normalized);

        return new ServiceArchitecture(
                basePackage,
                List.of("controller", "service", "repository", "dto", "entity", "config"),
                "Arquitetura Spring Boot em camadas para acelerar bootstrap do microserviço"
        );
    }
}
