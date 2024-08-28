package com.ericsson.cifwk.ve.plugins.jira.dto;

import java.util.Collection;

public class JiraEpicNode extends JiraAbstractNode {

    private static final String TYPE = "epic";

    public JiraEpicNode(String key, String summary, Collection<JiraNode> children) {
        super(key, summary, children);
    }

    @Override
    public String getType() {
        return TYPE;
    }
}
