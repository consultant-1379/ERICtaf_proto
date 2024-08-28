package com.ericsson.cifwk.ve.web;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import com.ericsson.cifwk.ve.actor.DashboardMasterActor;
import com.ericsson.cifwk.ve.actor.message.DashboardConnect;
import com.ericsson.cifwk.ve.actor.message.DashboardDirectPublish;
import com.ericsson.cifwk.ve.actor.message.DashboardDisconnect;
import com.ericsson.cifwk.ve.actor.message.DashboardPublish;
import com.ericsson.cifwk.ve.actor.message.DashboardSubscribe;
import com.ericsson.cifwk.ve.actor.message.DashboardUnsubscribe;
import com.ericsson.cifwk.ve.application.routing.MessageRouter;

public class DashboardService {

    private final ActorRef dashboardMaster;

    public DashboardService(ActorSystem actorSystem, MessageRouter jsonRouter) {
        dashboardMaster = actorSystem.actorOf(DashboardMasterActor.makeProps(jsonRouter));
    }

    public void connect(String sessionId, AtmosphereConnection connection) {
        dashboardMaster.tell(new DashboardConnect(sessionId, connection), ActorRef.noSender());
    }

    public void disconnect(String sessionId) {
        dashboardMaster.tell(new DashboardDisconnect(sessionId), ActorRef.noSender());
    }

    public void subscribe(String sessionId, String topic) {
        dashboardMaster.tell(new DashboardSubscribe(sessionId, topic), ActorRef.noSender());
    }

    public void unsubscribe(String sessionId, String topic) {
        dashboardMaster.tell(new DashboardUnsubscribe(sessionId, topic), ActorRef.noSender());
    }

    public void publish(String json) {
        dashboardMaster.tell(new DashboardPublish(json), ActorRef.noSender());
    }

    public void directPublish(String sessionId, String json) {
        dashboardMaster.tell(new DashboardDirectPublish(sessionId, json), ActorRef.noSender());
    }

}
