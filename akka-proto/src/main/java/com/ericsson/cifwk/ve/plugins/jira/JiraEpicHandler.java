package com.ericsson.cifwk.ve.plugins.jira;

import akka.actor.ActorRef;
import com.ericsson.cifwk.ve.application.Bootstrap;
import com.ericsson.cifwk.ve.plugins.DashboardMessageHandler;
import com.ericsson.cifwk.ve.plugins.jira.dto.JiraRequest;

import java.util.Map;

public class JiraEpicHandler implements DashboardMessageHandler {

    private static final String MESSAGE_NAME = "jiraEpic";

    @Override
    public String getMessageName() {
        return MESSAGE_NAME;
    }

    @Override
    public void handle(String sessionId, Map args) {
        String epicId = (String) args.get("epicId");
        ActorRef actor = Bootstrap.getInstance().getPluggedActor(JiraEpicActorFactory.ACTOR_NAME);
        actor.tell(new JiraRequest(sessionId, epicId), ActorRef.noSender());
    }

}
