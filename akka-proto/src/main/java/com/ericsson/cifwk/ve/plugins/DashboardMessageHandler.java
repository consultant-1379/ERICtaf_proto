package com.ericsson.cifwk.ve.plugins;

import java.util.Map;

public interface DashboardMessageHandler {
    String getMessageName();

    void handle(String sessionId, Map args);
}
