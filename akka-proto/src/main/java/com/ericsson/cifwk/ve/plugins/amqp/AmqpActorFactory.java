package com.ericsson.cifwk.ve.plugins.amqp;

import akka.actor.Props;
import com.ericsson.cifwk.ve.plugins.DashboardActorFactory;
import com.ericsson.cifwk.ve.infrastructure.config.Settings;

public class AmqpActorFactory implements DashboardActorFactory {

    public static final String ACTOR_NAME = "amqp-source";

    @Override
    public Props makeProps(Settings settings) {
        return Props.create(AmqpSourceActor.class,
                settings.getString("amqp.host"),
                settings.getInteger("amqp.port"),
                settings.getString("amqp.exchangeName"),
                settings.getString("amqp.username"),
                settings.getString("amqp.password"),
                settings.getString("amqp.routingKey")
        );
    }

    @Override
    public String getName() {
        return ACTOR_NAME;
    }
}
