package com.ericsson.cifwk.ve.application;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import com.ericsson.cifwk.ve.application.handler.SubscribeHandler;
import com.ericsson.cifwk.ve.application.handler.UnsubscribeHandler;
import com.ericsson.cifwk.ve.application.routing.EiffelMessageRouter;
import com.ericsson.cifwk.ve.application.routing.MessageRouter;
import com.ericsson.cifwk.ve.infrastructure.config.Settings;
import com.ericsson.cifwk.ve.infrastructure.config.SettingsProvider;
import com.ericsson.cifwk.ve.plugins.DashboardActorFactory;
import com.ericsson.cifwk.ve.plugins.DashboardMessageHandler;
import com.ericsson.cifwk.ve.plugins.DashboardResponseProcessor;
import com.ericsson.cifwk.ve.plugins.debug.DebugActorFactory;
import com.ericsson.cifwk.ve.plugins.debug.DebugSourceHandler;
import com.ericsson.cifwk.ve.web.DashboardService;
import com.ericsson.cifwk.ve.web.WebServer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.SetMultimap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Set;

/**
 *
 */
public final class Bootstrap {

    private static Bootstrap INSTANCE = new Bootstrap();

    private final Logger logger = LoggerFactory.getLogger(Bootstrap.class);

    private WebServer server;
    private ActorSystem actorSystem;
    private Map<String, ActorRef> pluggedActors;
    private ObjectMapper objectMapper;
    private MessageRouter messageRouter;
    private DashboardService dashboardService;
    private SetMultimap<String, DashboardMessageHandler> messageHandlers;
    private DashboardResponseProcessor responseProcessor;
    private EiffelMessageService eiffelMessageService;

    private Bootstrap() {
    }

    public WebServer getServer() {
        return server;
    }

    public MessageRouter getMessageRouter() {
        return messageRouter;
    }

    public ActorSystem getActorSystem() {
        return actorSystem;
    }

    public ActorRef getPluggedActor(String name) {
        return pluggedActors.get(name);
    }

    public ObjectMapper getObjectMapper() {
        return objectMapper;
    }

    public DashboardService getDashboardService() {
        return dashboardService;
    }

    public EiffelMessageService getEiffelMessageService() {
        return eiffelMessageService;
    }

    public Set<DashboardMessageHandler> getMessageHandlers(String name) {
        return messageHandlers.get(name);
    }

    public DashboardResponseProcessor getResponseProcessor() {
        return responseProcessor;
    }

    public static Bootstrap getInstance() {
        return INSTANCE;
    }

    public void init(DashboardConfiguration configuration) {
        Settings settings = loadSettings();
        startAkka(settings);
        startWebServer(settings);
        startServices(settings);
        setupMessageHandlers(settings, configuration);
        setupActors(settings, configuration);
        setupEventProcessor(settings, configuration);
    }

    private Settings loadSettings() {
        SettingsProvider provider = new SettingsProvider();
        return provider.loadSettings();
    }

    private void startAkka(Settings settings) {
        actorSystem = ActorSystem.create(settings.getString("akka.name"));
    }

    private void startWebServer(Settings settings) {
        server = new WebServer(settings);
        server.start();
    }

    private void startServices(Settings settings) {
        objectMapper = new ObjectMapper();
        eiffelMessageService = new EiffelMessageService(settings.getString("eiffel.version"));
        this.messageRouter = new EiffelMessageRouter(objectMapper, eiffelMessageService);
        dashboardService = new DashboardService(actorSystem, this.messageRouter);
    }

    private void setupMessageHandlers(Settings settings, DashboardConfiguration configuration) {
        Set<DashboardMessageHandler> handlerSet = configuration.getMessageHandlers();
        if (settings.getBoolean("debug.enabled")) {
            handlerSet.add(new DebugSourceHandler());
        }
        handlerSet.add(new SubscribeHandler());
        handlerSet.add(new UnsubscribeHandler());
        messageHandlers = HashMultimap.create();
        for (DashboardMessageHandler messageHandler : handlerSet) {
            logger.info("Registering message handler: " +
                    messageHandler.getMessageName() +
                    "[" + messageHandler.getClass().getName() + "]");
            messageHandlers.put(messageHandler.getMessageName(), messageHandler);
        }
    }

    private void setupActors(Settings settings, DashboardConfiguration configuration) {
        Set<DashboardActorFactory> factories = configuration.getActorFactories();
        if (settings.getBoolean("debug.enabled")) {
            factories.add(new DebugActorFactory());
        }
        ImmutableMap.Builder<String, ActorRef> mapBuilder = ImmutableMap.builder();
        for (DashboardActorFactory factory : factories) {
            Props props = factory.makeProps(settings);
            String name = factory.getName();
            logger.info("Registering actor: " + name +
                    "[" + props.actorClass().getName() + "]");
            ActorRef actorRef = actorSystem.actorOf(props, name);
            mapBuilder.put(name, actorRef);
        }
        pluggedActors = mapBuilder.build();
    }

    private void setupEventProcessor(Settings settings, DashboardConfiguration configuration) {
        responseProcessor = configuration.getResponseProcessor();
    }

    public void shutdown() {
        server.stop();
        actorSystem.shutdown();
    }

}
