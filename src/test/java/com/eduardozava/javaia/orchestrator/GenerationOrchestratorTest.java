package com.eduardozava.javaia.orchestrator;

import com.eduardozava.javaia.model.MicroserviceSpecRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class GenerationOrchestratorTest {

    @Autowired
    private GenerationOrchestrator orchestrator;

    @Test
    void shouldGenerateArtifactsWithSequentialAgents() {
        MicroserviceSpecRequest request = new MicroserviceSpecRequest(
                "order-service",
                "Gerar microserviço de pedidos",
                List.of("Order", "OrderItem"),
                List.of("POST /orders", "GET /orders/{id}")
        );

        var result = orchestrator.generate(request);

        assertThat(result.serviceName()).isEqualTo("order-service");
        assertThat(result.artifacts()).isNotEmpty();
        assertThat(result.reviewNotes()).isNotEmpty();
        assertThat(result.consolidatedOutput()).contains("Geração concluída");
    }
}
