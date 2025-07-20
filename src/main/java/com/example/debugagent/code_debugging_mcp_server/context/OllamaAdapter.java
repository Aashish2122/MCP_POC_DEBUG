package com.example.debugagent.code_debugging_mcp_server.context;

import com.example.debugagent.code_debugging_mcp_server.model.CodeAnalysis;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

@Component
public class OllamaAdapter {
    private static final Logger logger = LoggerFactory.getLogger(OllamaAdapter.class);

    @Value("${ollama.endpoint}")
    private String endpoint;

    @Autowired
    private CloseableHttpClient httpClient;

    public CodeAnalysis analyzeCode(String prompt) {
        try {
            HttpPost request = new HttpPost(endpoint + "/api/generate");
            request.setHeader("Content-Type", "application/json");
            request.setHeader("Accept", "application/json");

            String jsonPayload = String.format("""
                {
                    "model": "tinyllama",
                    "prompt": "%s",
                    "stream": false,
                    "options": {
                        "temperature": 0.3,
                        "num_ctx": 1024
                    }
                }""", sanitizePrompt(prompt));

            logger.info("Sending to Ollama: {}", jsonPayload);
            request.setEntity(new StringEntity(jsonPayload, StandardCharsets.UTF_8));

            try (var response = httpClient.execute(request)) {
                String jsonResponse = new String(
                        response.getEntity().getContent().readAllBytes(),
                        StandardCharsets.UTF_8
                );
                logger.info("Received from Ollama: {}", jsonResponse);
                return parseOllamaResponse(jsonResponse);
            }
        } catch (Exception e) {
            logger.error("Ollama analysis failed", e);
            return new CodeAnalysis(
                    "Analysis failed",
                    e.getMessage(),
                    CodeAnalysis.Status.ERROR
            );
        }
    }

    private String sanitizePrompt(String prompt) {
        return prompt.replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "");
    }

    private CodeAnalysis parseOllamaResponse(String json) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(json);

            if (!root.has("response")) {
                return new CodeAnalysis(
                        "Analysis incomplete",
                        "Ollama returned empty response",
                        CodeAnalysis.Status.ERROR
                );
            }

            String analysis = root.path("response").asText();
            return new CodeAnalysis(
                    "Analysis complete",
                    analysis,
                    CodeAnalysis.Status.SUCCESS
            );
        } catch (Exception e) {
            logger.error("Failed to parse Ollama response", e);
            return new CodeAnalysis(
                    "Analysis failed",
                    "Invalid response format: " + e.getMessage(),
                    CodeAnalysis.Status.ERROR
            );
        }
    }
}