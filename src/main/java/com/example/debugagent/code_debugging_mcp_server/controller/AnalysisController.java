package com.example.debugagent.code_debugging_mcp_server.controller;

import com.example.debugagent.code_debugging_mcp_server.context.AnalysisEngine;
import com.example.debugagent.code_debugging_mcp_server.protocol.DebugRequest;
import com.example.debugagent.code_debugging_mcp_server.protocol.DebugResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/analysis")
public class AnalysisController {
    private final AnalysisEngine analysisEngine;

    public AnalysisController(AnalysisEngine analysisEngine) {
        this.analysisEngine = analysisEngine;
    }

    @PostMapping
    public ResponseEntity<DebugResponse> analyze(@Valid @RequestBody DebugRequest request) {
        var result = analysisEngine.performAnalysis(
                request.getRepoUrl(),
                request.getErrorContext()
        );
        return ResponseEntity.ok(new DebugResponse(result, request.getRepoUrl()));
    }
}