package com.company.timesheet.model;

public class WorklogSuggestion {
    private String issueKey;
    private double hours;
    private String comment;
    private String started; // ISO 8601

    // getters and setters
    public String getIssueKey(){return issueKey;}
    public void setIssueKey(String k){this.issueKey=k;}
    public double getHours(){return hours;}
    public void setHours(double h){this.hours=h;}
    public String getComment(){return comment;}
    public void setComment(String c){this.comment=c;}
    public String getStarted(){return started;}
    public void setStarted(String s){this.started=s;}
}
