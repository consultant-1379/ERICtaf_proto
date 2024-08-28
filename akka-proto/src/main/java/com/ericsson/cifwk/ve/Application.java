package com.ericsson.cifwk.ve;

import com.ericsson.cifwk.ve.application.Dashboard;
import com.ericsson.cifwk.ve.application.DashboardConfiguration;
import com.ericsson.cifwk.ve.plugins.amqp.AmqpActorFactory;
import com.ericsson.cifwk.ve.plugins.er.EventRepositoryActorFactory;
import com.ericsson.cifwk.ve.plugins.er.EventRepositoryHandler;
import com.ericsson.cifwk.ve.plugins.jira.JiraEpicActorFactory;
import com.ericsson.cifwk.ve.plugins.jira.JiraEpicHandler;
import com.ericsson.cifwk.ve.plugins.taf.TafEventProcessor;

/**
 *
 */
public class Application {

    public static void main(String[] args) throws Exception {
        boolean barebone = Boolean.parseBoolean(System.getProperty("app.barebone"));
        DashboardConfiguration.Builder configBuilder = new DashboardConfiguration.Builder();
        if (!barebone) {
            configBuilder
                    .actor(new AmqpActorFactory())
                    .actor(new EventRepositoryActorFactory())
                    .messageHandler(new EventRepositoryHandler())
                    .responseProcessor(new TafEventProcessor());
        }

        DashboardConfiguration config = configBuilder
                .actor(new JiraEpicActorFactory())
                .messageHandler(new JiraEpicHandler())
                .build();
        Dashboard dashboard = new Dashboard(config);
        dashboard.start();
    }

}
