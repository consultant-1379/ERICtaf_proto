package com.ericsson.cifwk.ve.actor.message;

public class DashboardUnsubscribe implements DashboardMessage {

    private final String sessionId;
    private final String topic;

    public DashboardUnsubscribe(String sessionId, String topic) {
        this.sessionId = sessionId;
        this.topic = topic;
    }

    @Override
    public String getSessionId() {
        return sessionId;
    }

    public String getTopic() {
        return topic;
    }
}
