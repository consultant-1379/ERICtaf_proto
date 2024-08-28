package com.ericsson.cifwk.ve.actor.message;

public class DashboardDirectPublish implements DashboardMessage {

    private final String sessionId;
    private final String json;

    public DashboardDirectPublish(String sessionId, String json) {
        this.sessionId = sessionId;
        this.json = json;
    }

    @Override
    public String getSessionId() {
        return sessionId;
    }

    public String getJson() {
        return json;
    }
}
