package io.github.eduardozava.javaia.controller;

import io.github.eduardozava.javaia.model.EndpointSpec;
import io.github.eduardozava.javaia.model.GenerateRequest;
import io.github.eduardozava.javaia.model.GenerationResult;
import io.github.eduardozava.javaia.model.MicroserviceSpec;
import io.github.eduardozava.javaia.orchestrator.GenerationOrchestrator;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/generation")
public class GenerationController {

    private final GenerationOrchestrator generationOrchestrator;

    public GenerationController(GenerationOrchestrator generationOrchestrator) {
        this.generationOrchestrator = generationOrchestrator;
    }

    @PostMapping
    public ResponseEntity<GenerationResult> generate(@Valid @RequestBody GenerateRequest request) {
        List<EndpointSpec> endpoints = request.endpoints() == null
                ? List.of()
                : request.endpoints().stream()
                .map(e -> new EndpointSpec(e.method(), e.path(), e.description()))
                .toList();

        MicroserviceSpec spec = new MicroserviceSpec(request.serviceName(), request.description(), endpoints);
        return ResponseEntity.ok(generationOrchestrator.generate(spec));
    }
}
