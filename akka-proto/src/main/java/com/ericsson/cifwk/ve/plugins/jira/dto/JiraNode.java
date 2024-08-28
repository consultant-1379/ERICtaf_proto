package com.ericsson.cifwk.ve.plugins.jira.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Collection;

import static com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public interface JiraNode extends JiraResponse {
    String getKey();

    String getSummary();

    String getType();

    Collection<JiraNode> getChildren();
}
