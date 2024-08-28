package com.ericsson.cifwk.ve.plugins.er;

import akka.actor.Props;
import com.ericsson.cifwk.ve.plugins.DashboardActorFactory;
import com.ericsson.cifwk.ve.infrastructure.config.Settings;

public class EventRepositoryActorFactory implements DashboardActorFactory {

    public static final String ACTOR_NAME = "er-source";

    @Override
    public Props makeProps(Settings settings) {
        return Props.create(EventRepositoryActor.class,
                settings.getString("eventRepository.uri"),
                settings.getInteger("eventRepository.minutes"),
                settings.getString("eventRepository.eventType"));
    }

    @Override
    public String getName() {
        return ACTOR_NAME;
    }
}
