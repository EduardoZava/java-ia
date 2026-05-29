package com.eduardozava.javaia.infra.deepseek;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Map;

@Component
public class HttpDeepSeekClient implements DeepSeekClient {

    private final RestClient restClient;
    private final DeepSeekProperties properties;
    private final ObjectMapper objectMapper;

    public HttpDeepSeekClient(RestClient.Builder restClientBuilder, DeepSeekProperties properties, ObjectMapper objectMapper) {
        this.restClient = restClientBuilder.build();
        this.properties = properties;
        this.objectMapper = objectMapper;
    }

    @Override
    public String generateText(String prompt) {
        if (!StringUtils.hasText(properties.getApiKey())) {
            throw new DeepSeekConfigurationException("DeepSeek API key não configurada. Defina DEEPSEEK_API_KEY para habilitar geração por IA.");
        }

        Map<String, Object> payload = Map.of(
                "model", properties.getModel(),
                "messages", List.of(Map.of("role", "user", "content", prompt))
        );

        try {
            String response = restClient.post()
                    .uri(properties.getBaseUrl())
                    .header("Authorization", "Bearer " + properties.getApiKey())
                    .header("Content-Type", "application/json")
                    .body(payload)
                    .retrieve()
                    .body(String.class);

            if (response == null || response.isBlank()) {
                throw new DeepSeekClientException("DeepSeek retornou resposta vazia.");
            }

            JsonNode root = objectMapper.readTree(response);
            JsonNode content = root.path("choices").path(0).path("message").path("content");
            if (content.isMissingNode() || content.asText().isBlank()) {
                return "DeepSeek retornou resposta vazia para o prompt informado.";
            }
            return content.asText();
        } catch (DeepSeekClientException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new DeepSeekClientException("Falha ao chamar DeepSeek.", ex);
        }
    }
}
