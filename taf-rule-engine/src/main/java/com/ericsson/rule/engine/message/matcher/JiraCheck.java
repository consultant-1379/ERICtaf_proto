package com.ericsson.rule.engine.message.matcher;

import com.ericsson.rule.engine.configuration.Confidence;
import net.rcarz.jiraclient.Status;

import java.util.HashMap;
import java.util.Map;

/**
 *
 */
public class JiraCheck implements Runnable {

    private JiraFeed jiraFeed;

    public JiraCheck(JiraFeed jiraFeed) {
        this.jiraFeed = jiraFeed;
    }

    @Override
    public void run() {
        boolean run = true;
        while (run) {
            try {
                Map<String,Status> statusMap = jiraFeed.mapOfStories();
                Map<String,Boolean> translated = new HashMap<String, Boolean>();
                for (Map.Entry<String, Status> entry : statusMap.entrySet()) {
                    Status status = entry.getValue();
                    String statusName = status.getName();
                    if ("Open".equals(statusName) || "Reopened".equals(statusName)) {
                        Confidence.removeStory(entry.getKey());
                    } else {
                        translated.put(entry.getKey(), "Resolved".equals(statusName));
                    }
                }
                Confidence.updateJiraSatus(translated);
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

}
