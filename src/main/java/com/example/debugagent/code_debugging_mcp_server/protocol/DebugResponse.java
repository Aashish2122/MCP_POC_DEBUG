package com.example.debugagent.code_debugging_mcp_server.protocol;

import com.example.debugagent.code_debugging_mcp_server.model.CodeAnalysis;

public class DebugResponse {
    private String repository;
    private String analysis;
    private String status;

    public DebugResponse(CodeAnalysis analysis, String repoUrl) {
        this.repository = repoUrl;
        this.analysis = analysis.getDetailedReport();
        this.status = analysis.getStatus().name();
    }

    // Getters
    public String getRepository() { return repository; }
    public String getAnalysis() { return analysis; }
    public String getStatus() { return status; }
}