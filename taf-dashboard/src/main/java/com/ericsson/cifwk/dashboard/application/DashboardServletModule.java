package com.ericsson.cifwk.dashboard.application;

import com.google.inject.servlet.ServletModule;

import java.util.HashMap;
import java.util.Map;

public class DashboardServletModule extends ServletModule {

    @Override
    protected void configureServlets() {
        Map<String, String> sseQueueParams = new HashMap<>();
        sseQueueParams.put("heartBeatPeriod", "1");
        serve("/sse").with(SseQueueServlet.class, sseQueueParams);

        serve("/snapshot").with(SnapshotServlet.class);
    }

}
