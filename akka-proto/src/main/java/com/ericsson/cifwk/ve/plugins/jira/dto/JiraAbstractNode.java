package com.ericsson.cifwk.ve.plugins.jira.dto;

import com.google.common.base.Joiner;

import java.util.Collection;
import java.util.Collections;

public abstract class JiraAbstractNode implements JiraNode {

    private final String key;
    private final String summary;
    private final Collection<JiraNode> children;

    protected JiraAbstractNode(String key,
                               String summary,
                               Collection<JiraNode> children) {
        this.key = key;
        this.summary = summary;
        this.children = children;
    }

    @Override
    public String getKey() {
        return key;
    }

    @Override
    public String getSummary() {
        return summary;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        printLevel(builder, this);
        return builder.toString();
    }

    @Override
    public Collection<JiraNode> getChildren() {
        return children;
    }

    private void printLevel(StringBuilder builder, JiraNode node) {
        printLevel(builder, node, 0);
    }

    private void printLevel(StringBuilder builder, JiraNode node, int level) {
        String indent = Joiner.on("").join(Collections.nCopies(level, "    "));
        builder
                .append(indent)
                .append("- ")
                .append(node.getKey())
                .append(": ")
                .append(node.getSummary())
                .append("\n");
        Collection<JiraNode> nodeChildren = node.getChildren();
        if (nodeChildren != null) {
            for (JiraNode child : nodeChildren) {
                printLevel(builder, child, level + 1);
            }
        }
    }
}
