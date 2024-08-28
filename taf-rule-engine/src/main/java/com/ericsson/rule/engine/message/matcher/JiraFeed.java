package com.ericsson.rule.engine.message.matcher;

import net.rcarz.jiraclient.BasicCredentials;
import net.rcarz.jiraclient.Issue;
import net.rcarz.jiraclient.JiraClient;
import net.rcarz.jiraclient.JiraException;
import net.rcarz.jiraclient.Status;

import java.util.HashMap;
import java.util.Map;

public class JiraFeed {

    private static final String SPRINT = "832";

    private static JiraClient client;

    public JiraFeed() {
    }

    private JiraClient getClient() {
        if (client == null) {
            BasicCredentials credentials = new BasicCredentials("lciadm100", "lciadm100");
            client = new JiraClient("http://jira-oss.lmera.ericsson.se", credentials);
        }
        return client;
    }

    public Map<String, Status> mapOfStories() {
        Map<String, Status> result = new HashMap<String,Status>();
        try {
            for (Issue issue : getClient().searchIssues("Sprint =" + SPRINT + " and issuetype = Story").issues) {
                result.put(issue.getKey(), issue.getStatus());
            }
        } catch (JiraException e) {
            e.printStackTrace();
        }

        return result;
    }

}
