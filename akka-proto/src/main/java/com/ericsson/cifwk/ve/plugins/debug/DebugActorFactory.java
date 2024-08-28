package com.ericsson.cifwk.ve.plugins.debug;

import akka.actor.Props;
import com.ericsson.cifwk.ve.plugins.DashboardActorFactory;
import com.ericsson.cifwk.ve.infrastructure.config.Settings;

public class DebugActorFactory implements DashboardActorFactory {

    public static final String ACTOR_NAME = "debug-source";

    @Override
    public Props makeProps(Settings settings) {
        return Props.create(DebugSourceActor.class, settings.getString("debug.job"));
    }

    @Override
    public String getName() {
        return ACTOR_NAME;
    }
}
