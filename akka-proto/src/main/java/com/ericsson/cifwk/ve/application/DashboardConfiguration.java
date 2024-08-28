package com.ericsson.cifwk.ve.application;

import com.ericsson.cifwk.ve.plugins.DashboardActorFactory;
import com.ericsson.cifwk.ve.plugins.DashboardMessageHandler;
import com.ericsson.cifwk.ve.plugins.DashboardResponseProcessor;

import java.util.HashSet;
import java.util.Set;

public class DashboardConfiguration {

    private final Set<DashboardActorFactory> actorFactories;
    private final Set<DashboardMessageHandler> messageHandlers;
    private final DashboardResponseProcessor responseProcessor;

    private DashboardConfiguration(Builder builder) {
        actorFactories = builder.actorFactories;
        messageHandlers = builder.messageHandlers;
        responseProcessor = builder.responseProcessor;
    }

    public Set<DashboardActorFactory> getActorFactories() {
        return actorFactories;
    }

    public Set<DashboardMessageHandler> getMessageHandlers() {
        return messageHandlers;
    }

    public DashboardResponseProcessor getResponseProcessor() {
        return responseProcessor;
    }

    public static class Builder {

        private Set<DashboardActorFactory> actorFactories;
        private Set<DashboardMessageHandler> messageHandlers;
        private DashboardResponseProcessor responseProcessor;

        public Builder() {
            actorFactories = new HashSet<>();
            messageHandlers = new HashSet<>();
            responseProcessor = null;
        }

        public Builder actor(DashboardActorFactory actorFactory) {
            actorFactories.add(actorFactory);
            return this;
        }

        public Builder messageHandler(DashboardMessageHandler messageHandler) {
            messageHandlers.add(messageHandler);
            return this;
        }

        public Builder responseProcessor(DashboardResponseProcessor responseProcessor) {
            this.responseProcessor = responseProcessor;
            return this;
        }

        public DashboardConfiguration build() {
            return new DashboardConfiguration(this);
        }

    }
}
