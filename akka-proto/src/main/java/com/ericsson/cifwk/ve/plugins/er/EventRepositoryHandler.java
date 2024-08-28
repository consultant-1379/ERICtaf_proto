package com.ericsson.cifwk.ve.plugins.er;

import akka.actor.ActorRef;
import com.ericsson.cifwk.ve.actor.message.DashboardSubscribe;
import com.ericsson.cifwk.ve.application.Bootstrap;
import com.ericsson.cifwk.ve.plugins.DashboardMessageHandler;

import java.util.Map;

public class EventRepositoryHandler implements DashboardMessageHandler {

    private static final String MESSAGE_NAME = "subscribe";

    @Override
    public String getMessageName() {
        return MESSAGE_NAME;
    }

    @Override
    public void handle(String sessionId, Map args) {
        String topic = (String) args.get("topic");
        ActorRef actor = Bootstrap.getInstance().getPluggedActor(EventRepositoryActorFactory.ACTOR_NAME);
        actor.tell(new DashboardSubscribe(sessionId, topic), ActorRef.noSender());
    }

}
