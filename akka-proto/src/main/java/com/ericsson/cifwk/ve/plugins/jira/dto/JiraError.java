package com.ericsson.cifwk.ve.plugins.jira.dto;

public final class JiraError implements JiraResponse {

    private final String error;

    public JiraError(String error) {
        this.error = error;
    }

    public String getError() {
        return error;
    }

}
