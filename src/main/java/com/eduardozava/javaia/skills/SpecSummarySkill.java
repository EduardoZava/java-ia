package com.eduardozava.javaia.skills;

import com.eduardozava.javaia.model.AnalyzedSpec;
import com.eduardozava.javaia.model.MicroserviceSpecRequest;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class SpecSummarySkill implements Skill<MicroserviceSpecRequest, AnalyzedSpec> {

    @Override
    public AnalyzedSpec execute(MicroserviceSpecRequest input) {
        var entities = input.entities() == null ? List.<String>of() : input.entities();
        var endpoints = input.endpoints() == null ? List.<String>of() : input.endpoints();

        List<String> inferredCapabilities = new ArrayList<>();
        if (!entities.isEmpty()) {
            inferredCapabilities.add("CRUD para entidades principais");
        }
        if (!endpoints.isEmpty()) {
            inferredCapabilities.add("API REST com endpoints explícitos");
        }
        inferredCapabilities.add("Observabilidade básica e validação de entrada");

        return new AnalyzedSpec(
                input.serviceName().trim(),
                input.description().trim(),
                entities,
                endpoints,
                inferredCapabilities
        );
    }
}
