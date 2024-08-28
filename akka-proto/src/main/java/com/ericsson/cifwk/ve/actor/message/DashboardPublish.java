package com.ericsson.cifwk.ve.actor.message;

public class DashboardPublish implements DashboardMessage {

    private final String json;

    public DashboardPublish(String json) {
        this.json = json;
    }

    @Override
    public String getSessionId() {
        return null;
    }

    public String getJson() {
        return json;
    }
}
