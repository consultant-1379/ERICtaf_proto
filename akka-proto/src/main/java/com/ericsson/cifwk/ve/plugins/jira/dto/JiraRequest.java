package com.ericsson.cifwk.ve.plugins.jira.dto;

/**
 *
 */
public final class JiraRequest {

    private final String sessionId;
    private String epicId;

    public JiraRequest(String sessionId, String epicId) {
        this.sessionId = sessionId;
        this.epicId = epicId;
    }

    public String getSessionId() {
        return sessionId;
    }

    public String getEpicId() {
        return epicId;
    }

    @Override
    public String toString() {
        return "JiraRequest{" +
                "sessionId='" + sessionId + '\'' +
                ", epicId='" + epicId + '\'' +
                '}';
    }
}
