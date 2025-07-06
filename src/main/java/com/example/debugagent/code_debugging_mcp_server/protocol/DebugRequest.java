package com.example.debugagent.code_debugging_mcp_server.protocol;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class DebugRequest {
    @NotBlank(message = "Repository URL is required")
    @Pattern(regexp = "https://github\\.com/[^/]+/[^/]+",
            message = "Must be a valid GitHub URL")
    private String repoUrl;

    private String errorContext;

    // Getters and setters
    public String getRepoUrl() { return repoUrl; }
    public void setRepoUrl(String repoUrl) { this.repoUrl = repoUrl; }
    public String getErrorContext() { return errorContext; }
    public void setErrorContext(String errorContext) { this.errorContext = errorContext; }
}