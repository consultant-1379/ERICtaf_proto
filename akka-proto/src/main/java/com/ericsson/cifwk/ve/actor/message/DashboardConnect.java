package com.ericsson.cifwk.ve.actor.message;

import com.ericsson.cifwk.ve.application.DashboardConnection;

public class DashboardConnect implements DashboardMessage {

    private final String sessionId;
    private final DashboardConnection connection;

    public DashboardConnect(String sessionId, DashboardConnection connection) {
        this.sessionId = sessionId;
        this.connection = connection;
    }

    @Override
    public String getSessionId() {
        return sessionId;
    }

    public DashboardConnection getConnection() {
        return connection;
    }
}
