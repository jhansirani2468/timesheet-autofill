package com.company.timesheet.controller;

import com.company.timesheet.model.WorklogSuggestion;
import com.company.timesheet.service.TimesheetService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/autofill")
public class TimesheetController {
    private final TimesheetService service;
    public TimesheetController(TimesheetService service){ this.service = service; }

    @PostMapping("/suggest")
    public ResponseEntity<List<WorklogSuggestion>> suggest(@RequestParam String accountId) throws Exception {
        List<WorklogSuggestion> suggestions = service.generateSuggestions(accountId);
        return ResponseEntity.ok(suggestions);
    }

    @PostMapping("/submit")
    public ResponseEntity<String> submit(@RequestParam String accountId, @RequestBody List<WorklogSuggestion> logs) throws Exception {
        service.submitWorklogs(accountId, logs);
        return ResponseEntity.ok("submitted");
    }
}
