package project.seleniumEasy.example;

import master.web;
import org.testng.annotations.Test;

public class jiraRestAPI extends web {
    @Test
    void createIssueOnJira() {
        driver.get("https://google.com");
        if (!driver.getTitle().contains("google"))
            /*
            * url: jira's url
            * jira token
            * key, example 'Software Test Project (STP)' key is STP
            * summary: issue's title
            * description: issue's description
            * issue type: Bug, Story, ...
            * priority:  * (1) : Highest
                         * (2) : High
                         * (3) : Medium
                         * (4) : Low
                         * (5) : Lowest
            * label
            * originalEstimate
            * remainingEstimate
            * duedate
            * assignee
            * more detail about Jira REST api: https://developer.atlassian.com/cloud/jira/platform/rest/
             * */
            createIssue("", "", "", "", "", "", 1, "",
                    "", "", "", "");
    }
}
