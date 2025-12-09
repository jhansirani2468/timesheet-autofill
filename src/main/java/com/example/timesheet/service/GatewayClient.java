package com.example.timesheet.service;

import com.example.timesheet.dto.IssueDto;
import com.example.timesheet.dto.WorklogSuggestion;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class GatewayClient {

    private final WebClient client;
    private final ObjectMapper mapper = new ObjectMapper();

    public GatewayClient(WebClient client) {
        this.client = client;
    }

    public List<WorklogSuggestion> generateSuggestions(List<IssueDto> issues, String accountId) throws Exception {

        Map<String, Object> body = new HashMap<>();
        body.put("issues", issues);
        body.put("jiraAccountId", accountId);

        String resp = client.post()
                .uri("http://localhost:5001/generate")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(body)
                .retrieve()
                .bodyToMono(String.class)
                .block();

        return mapper.readValue(resp, new TypeReference<List<WorklogSuggestion>>() {});
    }
}
