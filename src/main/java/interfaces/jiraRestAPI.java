package interfaces;

public interface jiraRestAPI {
    void createIssue(String url, String jiraToken, String key, String summary, String description, String issueType,
                     int priority, String label, String originalEstimate, String remainingEstimate, String duedate, String assignee);
}
