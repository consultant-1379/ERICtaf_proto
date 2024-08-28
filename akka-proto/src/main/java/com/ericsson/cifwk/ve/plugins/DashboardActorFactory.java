package com.ericsson.cifwk.ve.plugins;

import akka.actor.Props;
import com.ericsson.cifwk.ve.infrastructure.config.Settings;

public interface DashboardActorFactory {
    Props makeProps(Settings settings);

    String getName();
}
