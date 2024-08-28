package com.ericsson.cifwk.ve.plugins.jira;

import akka.actor.Props;
import com.ericsson.cifwk.ve.plugins.DashboardActorFactory;
import com.ericsson.cifwk.ve.infrastructure.config.Settings;

public class JiraEpicActorFactory implements DashboardActorFactory {

    public static final String ACTOR_NAME = "jira-source";

    @Override
    public Props makeProps(Settings settings) {
        return Props.create(JiraEpicActor.class,
                settings.getString("jira.username"),
                settings.getString("jira.password"),
                settings.getString("jira.uri"),
                settings.getString("jira.fields.storyPoints")
        );
    }

    @Override
    public String getName() {
        return ACTOR_NAME;
    }
}
