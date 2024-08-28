package com.ericsson.cifwk.ve.actor.message;

public class DashboardDisconnect implements DashboardMessage {

    private final String sessionId;

    public DashboardDisconnect(String sessionId) {
        this.sessionId = sessionId;
    }

    @Override
    public String getSessionId() {
        return sessionId;
    }
}
