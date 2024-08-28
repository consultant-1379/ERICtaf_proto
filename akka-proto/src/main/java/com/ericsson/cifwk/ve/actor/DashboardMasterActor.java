package com.ericsson.cifwk.ve.actor;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;
import com.ericsson.cifwk.ve.actor.message.DashboardConnect;
import com.ericsson.cifwk.ve.actor.message.DashboardDirectPublish;
import com.ericsson.cifwk.ve.actor.message.DashboardDisconnect;
import com.ericsson.cifwk.ve.actor.message.DashboardMessage;
import com.ericsson.cifwk.ve.actor.message.DashboardPublish;
import com.ericsson.cifwk.ve.actor.message.DashboardSubscribe;
import com.ericsson.cifwk.ve.actor.message.DashboardUnsubscribe;
import com.ericsson.cifwk.ve.application.DashboardConnection;
import com.ericsson.cifwk.ve.application.routing.MessageRouter;

import java.util.HashMap;
import java.util.Map;

public class DashboardMasterActor extends UntypedActor {

    private final Map<String, ActorRef> dashboards;
    private final MessageRouter router;

    public DashboardMasterActor(Map<String, ActorRef> dashboards, MessageRouter router) {
        this.dashboards = dashboards;
        this.router = router;
    }

    public static Props makeProps(MessageRouter router) {
        return Props.create(DashboardMasterActor.class, new HashMap<>(), router);
    }

    @Override
    public void onReceive(Object message) throws Exception {
        if (message instanceof DashboardConnect) {
            connect((DashboardConnect) message);
        } else if (message instanceof DashboardDisconnect) {
            disconnect((DashboardDisconnect) message);
        } else if (message instanceof DashboardSubscribe) {
            propagate((DashboardSubscribe) message);
        } else if (message instanceof DashboardUnsubscribe) {
            propagate((DashboardUnsubscribe) message);
        } else if (message instanceof DashboardPublish) {
            publish((DashboardPublish) message);
        } else if (message instanceof DashboardDirectPublish) {
            directPublish((DashboardDirectPublish) message);
        } else {
            unhandled(message);
        }
    }

    private void propagate(DashboardMessage message) {
        String sessionId = message.getSessionId();
        ActorRef dashboard = dashboards.get(sessionId);
        if (dashboard != null) {
            dashboard.tell(message, self());
        }
    }

    private void connect(DashboardConnect message) {
        DashboardConnection connection = message.getConnection();
        String sessionId = message.getSessionId();
        ActorRef actor = context().actorOf(DashboardActor.makeProps(connection, router));
        dashboards.put(sessionId, actor);
    }

    private void disconnect(DashboardDisconnect message) {
        String sessionId = message.getSessionId();
        ActorRef dashboard = dashboards.get(sessionId);
        if (dashboard != null) {
            dashboard.tell(message, self());
            dashboards.remove(sessionId);
        }
    }

    private void publish(DashboardPublish message) {
        String json = message.getJson();
        router.publish(json);
    }

    private void directPublish(DashboardDirectPublish message) {
        String sessionId = message.getSessionId();
        String json = message.getJson();
        ActorRef dashboard = dashboards.get(sessionId);
        dashboard.tell(new DashboardPublish(json), self());
    }
}
