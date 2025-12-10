package com.company.timesheet.service;

import com.company.timesheet.model.WorklogSuggestion;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.*;
import java.util.HashMap;
import java.util.Map;

@Service
public class TempoClient {
    private final WebClient webClient = WebClient.create();
    private final String baseUrl;
    private final String token;

    public TempoClient(@Value("${app.tempo.base-url}") String baseUrl,
                       @Value("${app.tempo.api-token}") String token) {
        this.baseUrl = baseUrl;
        this.token = token;
    }

    public void submitWorklog(WorklogSuggestion w, String accountId) {
        // Build simple payload (adjust keys per your Tempo API version)
        Map<String,Object> payload = new HashMap<>();
        payload.put("issueKey", w.getIssueKey());
        payload.put("timeSpentSeconds", (int)(w.getHours()*3600));
        // started expected as ISO string - Tempo may want startDate + startTime
        payload.put("startDate", w.getStarted().substring(0,10));
        payload.put("startTime", w.getStarted().substring(11,19));
        payload.put("description", w.getComment());
        payload.put("authorAccountId", accountId);

        webClient.post()
                .uri(baseUrl + "/worklogs")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(payload)
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }
}
