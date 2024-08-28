package com.ericsson.cifwk.ve.application.handler;

import com.ericsson.cifwk.ve.application.Bootstrap;
import com.ericsson.cifwk.ve.plugins.DashboardMessageHandler;
import com.ericsson.cifwk.ve.web.DashboardService;

import java.util.Map;

public class SubscribeHandler implements DashboardMessageHandler {

    private static final String MESSAGE_NAME = "subscribe";

    @Override
    public String getMessageName() {
        return MESSAGE_NAME;
    }

    @Override
    public void handle(String sessionId, Map args) {
        DashboardService dashboardService = Bootstrap.getInstance().getDashboardService();
        dashboardService.subscribe(sessionId, (String) args.get("topic"));
    }

}
