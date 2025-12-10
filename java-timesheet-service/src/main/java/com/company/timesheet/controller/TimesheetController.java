package com.company.timesheet.controller;

import com.company.timesheet.dto.IssueDto;
import com.company.timesheet.dto.WorklogSuggestion;
import com.company.timesheet.service.GatewayClient;
import com.company.timesheet.service.JiraClient;
import com.company.timesheet.service.TempoClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/autofill")
public class AutoFillController {

    private final JiraClient jira;
    private final GatewayClient gateway;
    private final TempoClient tempo;

    public AutoFillController(JiraClient jira, GatewayClient gateway, TempoClient tempo) {
        this.jira = jira;
        this.gateway = gateway;
        this.tempo = tempo;
    }

    @PostMapping("/suggest")
    public ResponseEntity<List<WorklogSuggestion>> suggest(@RequestParam String accountId) throws Exception {
        List<IssueDto> issues = jira.fetchAssignedIssues(accountId);
        List<WorklogSuggestion> suggestions = gateway.generateSuggestions(issues, accountId);
        return ResponseEntity.ok(suggestions);
    }

    @PostMapping("/submit")
    public ResponseEntity<String> submit(@RequestParam String accountId, @RequestBody List<WorklogSuggestion> logs) throws Exception {
        tempo.submitWorklogs(accountId, logs);
        return ResponseEntity.ok("Worklogs submitted");
    }
}
