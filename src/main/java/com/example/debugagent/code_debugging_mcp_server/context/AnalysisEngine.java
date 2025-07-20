// src/main/java/com/example/debugagent/code_debugging_mcp_server/context/AnalysisEngine.java
package com.example.debugagent.code_debugging_mcp_server.context;

import com.example.debugagent.code_debugging_mcp_server.model.CodeAnalysis;
import com.example.debugagent.code_debugging_mcp_server.model.RepositoryContent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AnalysisEngine {
    private final CodeFetcher codeFetcher;
    private final OllamaAdapter ollamaAdapter;

    public AnalysisEngine(CodeFetcher codeFetcher, OllamaAdapter ollamaAdapter) {
        this.codeFetcher = codeFetcher;
        this.ollamaAdapter = ollamaAdapter;
    }

    public CodeAnalysis performAnalysis(String repoUrl, String errorContext) {
        try {
            log.debug("");
            System.out.println("Going to fetch repository");

            RepositoryContent repoContent = codeFetcher.fetchRepository(repoUrl);
            System.out.println("fetch repository complete");

            String prompt = buildPrompt(repoContent, errorContext);
            log.info("Going to analyze");
            return ollamaAdapter.analyzeCode(prompt);
        } catch (Exception e) {
            return new CodeAnalysis(
                    "Analysis failed",
                    e.getMessage(),
                    CodeAnalysis.Status.ERROR
            );
        }
    }

    private String buildPrompt(RepositoryContent repo, String errorContext) {
        StringBuilder sb = new StringBuilder();
        sb.append("");

        if (errorContext != null && !errorContext.isEmpty()) {
            sb.append("").append(errorContext);
        }

        sb.append("\n\nFiles:\n");
        repo.getFiles().forEach((path, content) -> {
            sb.append("\n=== ").append(path).append(" ===\n");
            sb.append(content).append("\n");
        });

        return sb.toString();
    }
}