package com.example.debugagent.code_debugging_mcp_server.context;

import com.example.debugagent.code_debugging_mcp_server.model.RepositoryContent;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GitHub;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Service
public class CodeFetcher {
    public RepositoryContent fetchRepository(String repoUrl) throws IOException {
        String[] parts = repoUrl.split("/");
        String owner = parts[parts.length - 2];
        String repoName = parts[parts.length - 1];

        GitHub github = GitHub.connectAnonymously();
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
                        }
                    }
                });

        return new RepositoryContent(owner, repoName, files);
    }
}