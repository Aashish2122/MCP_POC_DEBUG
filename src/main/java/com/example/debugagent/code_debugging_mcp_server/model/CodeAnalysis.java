package com.example.debugagent.code_debugging_mcp_server.model;

public class CodeAnalysis {
    private final String summary;
    private final String detailedReport;
    private final Status status;

    public enum Status { SUCCESS, ERROR }

    // Constructor with validation
    public CodeAnalysis(String summary, String detailedReport, Status status) {
        this.summary = summary == null ? "" : summary;
        this.detailedReport = detailedReport == null ? "" : detailedReport;
        this.status = status == null ? Status.ERROR : status;
    }

    // Getters
    public String getSummary() { return summary; }
    public String getDetailedReport() { return detailedReport; }
    public Status getStatus() { return status; }
}