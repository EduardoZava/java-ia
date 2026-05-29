package io.github.eduardozava.javaia.skills;

import io.github.eduardozava.javaia.model.GenerationContext;
import org.springframework.stereotype.Component;

@Component
public class SpringStructureSkill implements Skill {

    private static final String SPRING_BOOT_VERSION = "3.3.5";

    @Override
    public void apply(GenerationContext context) {
        String serviceName = context.spec().serviceName();
        String packageName = sanitizeName(serviceName);
        String base = "output/%s/src/main/java/com/example/%s".formatted(serviceName, packageName);

        context.addArtifact("output/%s/pom.xml".formatted(serviceName), """
                <project>
                  <modelVersion>4.0.0</modelVersion>
                  <parent>
                    <groupId>org.springframework.boot</groupId>
                    <artifactId>spring-boot-starter-parent</artifactId>
                    <version>%s</version>
                    <relativePath/>
                  </parent>
                  <groupId>com.example</groupId>
                  <artifactId>%s</artifactId>
                  <version>0.0.1-SNAPSHOT</version>
                  <properties>
                    <java.version>17</java.version>
                  </properties>
                  <dependencies>
                    <dependency>
                      <groupId>org.springframework.boot</groupId>
                      <artifactId>spring-boot-starter-web</artifactId>
                    </dependency>
                  </dependencies>
                  <build>
                    <plugins>
                      <plugin>
                        <groupId>org.springframework.boot</groupId>
                        <artifactId>spring-boot-maven-plugin</artifactId>
                      </plugin>
                    </plugins>
                  </build>
                </project>
                """.formatted(SPRING_BOOT_VERSION, serviceName));

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
