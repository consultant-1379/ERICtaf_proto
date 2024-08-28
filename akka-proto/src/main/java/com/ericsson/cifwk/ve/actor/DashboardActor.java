package com.ericsson.cifwk.ve.actor;

import akka.actor.PoisonPill;
import akka.actor.Props;
import akka.actor.UntypedActor;
import com.ericsson.cifwk.ve.actor.message.DashboardDisconnect;
import com.ericsson.cifwk.ve.actor.message.DashboardPublish;
import com.ericsson.cifwk.ve.actor.message.DashboardSubscribe;
import com.ericsson.cifwk.ve.actor.message.DashboardUnsubscribe;
import com.ericsson.cifwk.ve.application.DashboardConnection;
import com.ericsson.cifwk.ve.application.DashboardConsumer;
import com.ericsson.cifwk.ve.application.routing.MessageRouter;

public class DashboardActor extends UntypedActor {

    private final DashboardConsumer consumer;
    private final MessageRouter router;

    public DashboardActor(DashboardConnection connection, MessageRouter router) {
        this.consumer = new DashboardConsumer(connection);
        this.router = router;
    }

    public static Props makeProps(DashboardConnection connection, MessageRouter router) {
        return Props.create(DashboardActor.class, connection, router);
    }

    @Override
    public void onReceive(Object message) throws Exception {
        if (message instanceof DashboardSubscribe) {
            subscribe((DashboardSubscribe) message);
        } else if (message instanceof DashboardUnsubscribe) {
            unsubscribe((DashboardUnsubscribe) message);
        } else if (message instanceof DashboardPublish) {
            publish((DashboardPublish) message);
        } else if (message instanceof DashboardDisconnect) {
            disconnect((DashboardDisconnect) message);
        } else {
            unhandled(message);
        }
    }

    private void subscribe(DashboardSubscribe message) {
        router.subscribe(message.getTopic(), consumer);
    }

    private void unsubscribe(DashboardUnsubscribe message) {
        router.unsubscribe(message.getTopic(), consumer);
    }

    private void publish(DashboardPublish message) {
        String json = message.getJson();
        consumer.consume(json);
    }

    private void disconnect(DashboardDisconnect message) {
        router.unsubscribe(consumer);
        self().tell(PoisonPill.getInstance(), self());
    }

}
