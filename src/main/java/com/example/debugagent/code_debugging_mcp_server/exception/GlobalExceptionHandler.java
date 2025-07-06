package com.example.debugagent.code_debugging_mcp_server.exception;

import com.example.debugagent.code_debugging_mcp_server.model.CodeAnalysis;
import com.example.debugagent.code_debugging_mcp_server.protocol.DebugResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(Exception.class)
    public ResponseEntity<DebugResponse> handleException(Exception e) {
        return ResponseEntity.internalServerError()
                .body(new DebugResponse(
                        new CodeAnalysis("Error", e.getMessage(), CodeAnalysis.Status.ERROR),
                        null
                ));
    }
}