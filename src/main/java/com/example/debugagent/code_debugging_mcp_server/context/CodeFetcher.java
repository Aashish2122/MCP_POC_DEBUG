package com.example.debugagent.code_debugging_mcp_server.context;

import com.example.debugagent.code_debugging_mcp_server.model.RepositoryContent;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GitHub;
import org.kohsuke.github.GitHubBuilder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;

@Service
public class CodeFetcher {
    @Value("${github.token}") // Add to application.properties
    private String githubToken;
    public RepositoryContent fetchRepository(String repoUrl) throws IOException {
        String[] parts = repoUrl.split("/");
        String owner = parts[parts.length - 2];
        System.out.println("owner "+ owner);
        String repoName = parts[parts.length - 1];
        System.out.println("repoName "+ repoName);

        GitHub github = new GitHubBuilder()
                .withOAuthToken(githubToken)
                .build();
        System.out.println("connection established with github");
        GHRepository repo = github.getRepository(owner + "/" + repoName);

        Map<String, String> files = new HashMap<>();
        repo.getTreeRecursive(repo.getDefaultBranch(), 1).getTree()
                .forEach(entry -> {
                    if ("blob".equals(entry.getType())) {
                        try (InputStream contentStream = entry.readAsBlob()) {
                            files.put(
                                    entry.getPath(),
                                    new String(contentStream.readAllBytes(), StandardCharsets.UTF_8)
                            );
                        } catch (IOException e) {
                            // Skip files that can't be read
                            System.out.println(e.getMessage());
                        }
                    }
                });

        return new RepositoryContent(owner, repoName, files);
    }
}