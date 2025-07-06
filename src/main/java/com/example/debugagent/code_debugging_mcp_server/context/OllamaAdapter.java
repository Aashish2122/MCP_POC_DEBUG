package com.example.debugagent.code_debugging_mcp_server.context;

import com.example.debugagent.code_debugging_mcp_server.model.CodeAnalysis;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

@Component
public class OllamaAdapter {
    @Value("${ollama.endpoint}")
    private String endpoint;

    @Autowired
    private CloseableHttpClient httpClient;

    public CodeAnalysis analyzeCode(String prompt) {
        try {
            HttpPost request = new HttpPost(endpoint + "/api/generate");
            request.setHeader("Content-Type", "application/json");
            request.setEntity(new StringEntity(
                    String.format("{\"model\":\"llama2:7b\",\"prompt\":\"%s\"}",
                            sanitizePrompt(prompt)),
                    StandardCharsets.UTF_8
            ));

            try (var response = httpClient.execute(request)) {
                String json = new String(
                        response.getEntity().getContent().readAllBytes(),
                        StandardCharsets.UTF_8
                );
                return parseOllamaResponse(json);
            }
        } catch (Exception e) {
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
        String analysis = json.contains("\"response\":")
                ? json.split("\"response\":")[1].replaceAll("\"}", "").trim()
                : "No analysis generated";
        return new CodeAnalysis("Analysis complete", analysis, CodeAnalysis.Status.SUCCESS);
    }
}