package com.ericsson.cifwk.ve.plugins.jira;

import com.ericsson.cifwk.ve.plugins.jira.dto.*;
import net.rcarz.jiraclient.BasicCredentials;
import net.rcarz.jiraclient.Issue;
import net.rcarz.jiraclient.JiraClient;
import net.rcarz.jiraclient.JiraException;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public final class Jira {

    public static final double DEFAULT_STORY_POINTS = 1;

    private final String username;
    private final String password;
    private final String uri;
    private final String storyPointsField;
    private JiraClient client;

    public Jira(String username, String password, String uri, String storyPointsField) {
        this.username = username;
        this.password = password;
        this.uri = uri;
        this.storyPointsField = storyPointsField;
    }

    public void connect() {
        BasicCredentials credentials = new BasicCredentials(username, password);
        client = new JiraClient(uri, credentials);
    }

    public JiraResponse getEpicStructure(String epicId) {
        String epicSummary;
        List<Issue> stories;
        try {
            epicSummary = getEpicSummary(epicId);
            stories = getStories(epicId);
        } catch (JiraException e) {
            return new JiraError(e.getMessage());
        }
        List<JiraNode> storyNodes = new ArrayList<>();
        for (Issue story : stories) {
            String key = story.getKey();
            String summary = story.getSummary();
            double storyPoints = getStoryPoints(story);
            JiraAbstractNode storyNode = new JiraStoryNode(key, summary, storyPoints);
            storyNodes.add(storyNode);
        }
        return new JiraEpicNode(epicId, epicSummary, storyNodes);
    }

    private String getEpicSummary(String epicId) throws JiraException {
        Issue.SearchResult result = client.searchIssues("issueType = Epic AND id = " + epicId);
        Iterator<Issue> resultIterator = result.issues.iterator();
        if (resultIterator.hasNext()) {
            String summary = resultIterator.next().getSummary();
            if (summary != null) {
                return summary;
            }
        }
        throw new JiraException("Failed to find summary for " + epicId);
    }

    private List<Issue> getStories(String epicId) throws JiraException {
        Issue.SearchResult result = client.searchIssues("issueType = Story AND \"Epic Link\" = " + epicId);
        return result.issues;
    }

    private double getStoryPoints(Issue story) {
        Object field = story.getField(storyPointsField);
        if (field instanceof Double) {
            return (double) field;
        }
        return DEFAULT_STORY_POINTS;
    }

}
