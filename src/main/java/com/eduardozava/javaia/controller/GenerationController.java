package com.eduardozava.javaia.controller;

import com.eduardozava.javaia.model.GenerationResult;
import com.eduardozava.javaia.model.MicroserviceSpecRequest;
import com.eduardozava.javaia.orchestrator.GenerationOrchestrator;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/generation")
public class GenerationController {

    private final GenerationOrchestrator generationOrchestrator;

    public GenerationController(GenerationOrchestrator generationOrchestrator) {
        this.generationOrchestrator = generationOrchestrator;
    }

    @PostMapping
    public ResponseEntity<GenerationResult> generate(@Valid @RequestBody MicroserviceSpecRequest request) {
        return ResponseEntity.ok(generationOrchestrator.generate(request));
    }
}
