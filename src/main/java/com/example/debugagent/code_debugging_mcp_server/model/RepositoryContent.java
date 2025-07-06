package com.example.debugagent.code_debugging_mcp_server.model;

import java.util.Map;

public class RepositoryContent {
    private final String owner;
    private final String repoName;
    private final Map<String, String> files;

    public RepositoryContent(String owner, String repoName, Map<String, String> files) {
        this.owner = owner;
        this.repoName = repoName;
        this.files = Map.copyOf(files); // Defensive copy
    }

    // Getters
    public String getOwner() { return owner; }
    public String getRepoName() { return repoName; }
    public Map<String, String> getFiles() { return files; }
}