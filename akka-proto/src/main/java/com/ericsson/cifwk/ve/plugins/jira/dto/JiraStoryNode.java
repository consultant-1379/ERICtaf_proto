package com.ericsson.cifwk.ve.plugins.jira.dto;

import com.google.common.collect.ImmutableMap;

import java.util.Map;

public class JiraStoryNode extends JiraAbstractNode {

    private static final String TYPE = "story";

    private final double storyPoints;

    public JiraStoryNode(String key, String summary, double storyPoints) {
        super(key, summary, null);
        this.storyPoints = storyPoints;
    }

    private static Map<String, Object> attributes(double storyPoints) {
        return new ImmutableMap.Builder<String, Object>()
                .put("storyPoints", storyPoints)
                .build();
    }

    @Override
    public String getType() {
        return TYPE;
    }

    public double getStoryPoints() {
        return storyPoints;
    }

}
