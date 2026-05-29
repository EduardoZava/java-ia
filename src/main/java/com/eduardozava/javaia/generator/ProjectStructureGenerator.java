package com.eduardozava.javaia.generator;

import com.eduardozava.javaia.model.AnalyzedSpec;
import com.eduardozava.javaia.model.ApiDesign;
import com.eduardozava.javaia.model.GeneratedArtifact;
import com.eduardozava.javaia.model.ServiceArchitecture;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Component
public class ProjectStructureGenerator {

    private final ResourceLoader resourceLoader;

    public ProjectStructureGenerator(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    public List<GeneratedArtifact> generate(AnalyzedSpec spec, ServiceArchitecture architecture, ApiDesign apiDesign) {
        List<GeneratedArtifact> artifacts = new ArrayList<>(apiDesign.artifacts());

        String serviceClass = toPascalCase(spec.serviceName()) + "Service";
        String servicePackage = architecture.basePackage() + ".service";
        String servicePath = "src/main/java/" + servicePackage.replace('.', '/') + "/" + serviceClass + ".java";

        String template = readTemplate("classpath:templates/service-class.txt");
        String serviceContent = template
                .replace("{{package}}", servicePackage)
                .replace("{{className}}", serviceClass)
                .replace("{{serviceName}}", spec.serviceName());

        artifacts.add(new GeneratedArtifact(servicePath, serviceContent));
        artifacts.add(new GeneratedArtifact("README-generated.md", "# " + spec.serviceName() + "\n\n" + spec.normalizedDescription()));
        return artifacts;
    }

    private String readTemplate(String location) {
        try {
            return resourceLoader.getResource(location)
                    .getContentAsString(StandardCharsets.UTF_8);
        } catch (IOException ex) {
            throw new RuntimeException("Não foi possível ler template: " + location, ex);
        }
    }

    private String toPascalCase(String value) {
        if (value == null || value.isBlank()) {
            return "Generated";
        }
        String[] parts = value.trim().split("[^a-zA-Z0-9]+");
        StringBuilder builder = new StringBuilder();
        for (String part : parts) {
            if (!part.isBlank()) {
                builder.append(Character.toUpperCase(part.charAt(0)));
                if (part.length() > 1) {
                    builder.append(part.substring(1));
                }
            }
        }
        return builder.isEmpty() ? "Generated" : builder.toString();
    }
}
