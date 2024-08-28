package com.ericsson.cifwk.ve.actor.message;

public class DashboardSubscribe implements DashboardMessage {

    private final String sessionId;
    private final String topic;

    public DashboardSubscribe(String sessionId, String topic) {
        this.sessionId = sessionId;
        this.topic = topic;
    }

    public String getTopic() {
        return topic;
    }

    @Override
    public String getSessionId() {
        return sessionId;
    }
}
