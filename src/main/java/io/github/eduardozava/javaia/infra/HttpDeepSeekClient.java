package io.github.eduardozava.javaia.infra;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Optional;

@Component
public class HttpDeepSeekClient implements DeepSeekClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(HttpDeepSeekClient.class);

    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;
    private final String apiKey;
    private final String baseUrl;
    private final String model;
    private final int timeoutSeconds;

    public HttpDeepSeekClient(
            ObjectMapper objectMapper,
            @Value("${deepseek.api-key:${DEEPSEEK_API_KEY:}}") String apiKey,
            @Value("${deepseek.base-url:${DEEPSEEK_BASE_URL:https://api.deepseek.com}}") String baseUrl,
            @Value("${deepseek.model:${DEEPSEEK_MODEL:deepseek-chat}}") String model,
            @Value("${deepseek.timeout-seconds:${DEEPSEEK_TIMEOUT_SECONDS:30}}") int timeoutSeconds
    ) {
        this.httpClient = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(timeoutSeconds)).build();
        this.objectMapper = objectMapper;
        this.apiKey = apiKey;
        this.baseUrl = baseUrl;
        this.model = model;
        this.timeoutSeconds = timeoutSeconds;
    }

    @Override
    public Optional<String> complete(String prompt) {
        if (apiKey == null || apiKey.isBlank()) {
            return Optional.empty();
        }

        try {
            String body = objectMapper.createObjectNode()
                    .put("model", model)
                    .putArray("messages")
                    .add(objectMapper.createObjectNode()
                            .put("role", "user")
                            .put("content", prompt))
                    .toString();

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(baseUrl + "/chat/completions"))
                    .timeout(Duration.ofSeconds(timeoutSeconds))
                    .header("Authorization", "Bearer " + apiKey)
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(body))
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() >= 400) {
                LOGGER.warn("DeepSeek retornou status {}", response.statusCode());
                return Optional.empty();
            }

            JsonNode root = objectMapper.readTree(response.body());
            JsonNode content = root.path("choices").path(0).path("message").path("content");
            return content.isMissingNode() ? Optional.empty() : Optional.ofNullable(content.asText());
        } catch (Exception ex) {
            LOGGER.warn("Falha ao chamar DeepSeek: {}", ex.getMessage());
            return Optional.empty();
        }
    }

    @Override
    public String configurationStatus() {
        if (apiKey == null || apiKey.isBlank()) {
            return "DeepSeek desabilitado: defina DEEPSEEK_API_KEY para habilitar enriquecimento por IA.";
        }
        return "DeepSeek configurado em " + baseUrl;
    }
}
