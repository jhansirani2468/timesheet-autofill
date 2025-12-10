package com.company.timesheet.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.*;

@Service
public class JiraClient {
    private final WebClient webClient = WebClient.create();
    private final String baseUrl;
    private final String basicAuth;
    private final ObjectMapper mapper = new ObjectMapper();

    public JiraClient(@Value("${app.jira.base-url}") String baseUrl,
                      @Value("${app.jira.email}") String email,
                      @Value("${app.jira.api-token}") String token) {
        this.baseUrl = baseUrl;
        String auth = email + ":" + token;
        this.basicAuth = "Basic " + Base64.getEncoder().encodeToString(auth.getBytes());
    }

    /**
     * Returns a list of simple issue objects (maps). Minimal fields needed.
     */
    public List<Map<String,Object>> fetchAssignedIssues(String accountId) throws Exception {
        String jql = String.format("assignee = \"%s\" AND statusCategory != Done ORDER BY priority DESC", accountId);
        String url = baseUrl + "/rest/api/3/search?jql=" + java.net.URLEncoder.encode(jql, "UTF-8") + "&fields=summary,description,updated,customfield_10016,status";
        Mono<String> resp = webClient.get()
                .uri(url)
                .header(HttpHeaders.AUTHORIZATION, basicAuth)
                .header(HttpHeaders.ACCEPT, "application/json")
                .retrieve()
                .bodyToMono(String.class);
        String json = resp.block();
        JsonNode root = mapper.readTree(json);
        List<Map<String,Object>> out = new ArrayList<>();
        for (JsonNode issue: root.path("issues")) {
            Map<String,Object> m = new HashMap<>();
            m.put("key", issue.path("key").asText());
            m.put("summary", issue.path("fields").path("summary").asText(""));
            m.put("description", issue.path("fields").path("description").asText(""));
            m.put("updated", issue.path("fields").path("updated").asText(""));
            if (issue.path("fields").has("customfield_10016")) {
                m.put("storyPoints", issue.path("fields").path("customfield_10016").asDouble(0.0));
            }
            out.add(m);
        }
        return out;
    }
}
