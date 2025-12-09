package com.example.timesheet.service;

import com.example.timesheet.dto.WorklogSuggestion;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Mock Tempo API client for hackathon.
 */
@Service
public class TempoClient {

    public void submitWorklogs(String accountId, List<WorklogSuggestion> logs) {
        System.out.println("Submitting to Tempo for user " + accountId);
        logs.forEach(l -> {
            System.out.println(" - " + l.getIssueKey() + " : " + l.getHours() + "h");
        });
    }
}
