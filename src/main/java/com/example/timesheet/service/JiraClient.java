package com.example.timesheet.service;

import com.example.timesheet.dto.IssueDto;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Dummy implementation for Hackathon — replace with actual Jira API call.
 */
@Service
public class JiraClient {

    public List<IssueDto> fetchAssignedIssues(String accountId) {

        // Demo data
        IssueDto i1 = new IssueDto();
        i1.setKey("PROJ-101");
        i1.setSummary("Build UI");
        i1.setDescription("Implement the dashboard UI");
        i1.setStatus("In Progress");
        i1.setStoryPoints(3.0);

        IssueDto i2 = new IssueDto();
        i2.setKey("PROJ-102");
        i2.setSummary("Fix Login Bug");
        i2.setDescription("User cannot login intermittently");
        i2.setStatus("To Do");
        i2.setStoryPoints(2.0);

        return List.of(i1, i2);
    }
}
