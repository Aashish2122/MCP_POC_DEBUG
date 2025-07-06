package com.example.debugagent.code_debugging_mcp_server.config;

import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {
    @Bean
    public CloseableHttpClient httpClient() {
        return HttpClients.createDefault();
    }
}