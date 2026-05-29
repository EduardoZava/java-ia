package io.github.eduardozava.javaia.skills;

import io.github.eduardozava.javaia.model.GenerationContext;
import org.springframework.stereotype.Component;

@Component
public class SpringStructureSkill implements Skill {

    @Override
    public void apply(GenerationContext context) {
        String serviceName = context.spec().serviceName();
        String packageName = sanitizeName(serviceName);
        String base = "output/%s/src/main/java/com/example/%s".formatted(serviceName, packageName);

        context.addArtifact("output/%s/pom.xml".formatted(serviceName), """
                <project>
                  <modelVersion>4.0.0</modelVersion>
                  <groupId>com.example</groupId>
                  <artifactId>%s</artifactId>
                  <version>0.0.1-SNAPSHOT</version>
                </project>
                """.formatted(serviceName));

        context.addArtifact(base + "/Application.java", """
                package com.example.%s;

                import org.springframework.boot.SpringApplication;
                import org.springframework.boot.autoconfigure.SpringBootApplication;

                @SpringBootApplication
                public class Application {
                    public static void main(String[] args) {
                        SpringApplication.run(Application.class, args);
                    }
                }
                """.formatted(packageName));
    }

    private String sanitizeName(String input) {
        String sanitized = input == null ? "generated" : input.toLowerCase().replaceAll("[^a-z0-9]", "");
        return sanitized.isBlank() ? "generated" : sanitized;
    }
}
