package com.company.timesheet.service;

import com.company.timesheet.model.WorklogSuggestion;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.UUID;

@Service
public class TimesheetService {

    private final JiraClient jiraClient;
    private final PythonInvoker pythonInvoker;
    private final TempoClient tempoClient;
    private final ObjectMapper mapper = new ObjectMapper();

    public TimesheetService(JiraClient jiraClient, PythonInvoker pythonInvoker, TempoClient tempoClient) {
        this.jiraClient = jiraClient;
        this.pythonInvoker = pythonInvoker;
        this.tempoClient = tempoClient;
    }

    public List<WorklogSuggestion> generateSuggestions(String accountId) throws Exception {
        // 1. fetch issues
        List<?> issues = jiraClient.fetchAssignedIssues(accountId);

        // 2. write JSON to temp file and invoke python
        String payload = mapper.writeValueAsString(issues);
        String pythonOutput = pythonInvoker.invokeWithJson(payload);

        // 3. python returns JSON suggestions (array)
        List<WorklogSuggestion> suggestions = mapper.readValue(pythonOutput, new TypeReference<List<WorklogSuggestion>>() {});
        return suggestions;
    }

    public void submitWorklogs(String accountId, List<WorklogSuggestion> logs) throws Exception {
        for (WorklogSuggestion w: logs) {
            tempoClient.submitWorklog(w, accountId);
        }
    }
}
