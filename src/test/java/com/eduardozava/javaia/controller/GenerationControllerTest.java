package com.eduardozava.javaia.controller;

import com.eduardozava.javaia.model.GeneratedArtifact;
import com.eduardozava.javaia.model.GenerationResult;
import com.eduardozava.javaia.orchestrator.GenerationOrchestrator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(GenerationController.class)
class GenerationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GenerationOrchestrator generationOrchestrator;

    @Test
    void shouldReturnGenerationResult() throws Exception {
        GenerationResult mocked = new GenerationResult(
                "billing-service",
                List.of(new GeneratedArtifact("README-generated.md", "content")),
                List.of("ok"),
                "summary"
        );

        when(generationOrchestrator.generate(any())).thenReturn(mocked);

        mockMvc.perform(post("/api/generation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "serviceName":"billing-service",
                                  "description":"mvp",
                                  "entities":["Invoice"],
                                  "endpoints":["GET /invoices"]
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.serviceName").value("billing-service"))
                .andExpect(jsonPath("$.artifacts[0].path").value("README-generated.md"));
    }
}
