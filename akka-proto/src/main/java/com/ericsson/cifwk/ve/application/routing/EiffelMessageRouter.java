package com.ericsson.cifwk.ve.application.routing;

import com.ericsson.cifwk.ve.application.Bootstrap;
import com.ericsson.cifwk.ve.application.EiffelMessageService;
import com.ericsson.cifwk.ve.application.MessageConsumer;
import com.ericsson.cifwk.ve.application.dto.EiffelMessage;
import com.ericsson.cifwk.ve.application.dto.EiffelMessageWrapper;
import com.ericsson.cifwk.ve.plugins.DashboardResponseProcessor;
import com.ericsson.cifwk.ve.web.ServerResponseSerializer;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.SetMultimap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

public class EiffelMessageRouter implements MessageRouter {

    public static final String SENDER_NAME = "router";

    private final Logger logger = LoggerFactory.getLogger(EiffelMessageRouter.class);

    private final ObjectMapper objectMapper;
    private final EiffelMessageService eiffelMessageService;

    private final SetMultimap<JsonRoute, MessageConsumer> routeConsumers;
    private final SetMultimap<MessageConsumer, JsonRoute> consumerRoutes;

    public EiffelMessageRouter(ObjectMapper objectMapper, EiffelMessageService eiffelMessageService) {
        this.objectMapper = objectMapper;
        this.eiffelMessageService = eiffelMessageService;
        this.routeConsumers = HashMultimap.create();
        this.consumerRoutes = HashMultimap.create();
    }

    @Override
    public void subscribe(String topic, MessageConsumer routable) {
        JsonRoute route = JsonRoute.parse(topic);
        if (route != null) {
            routeConsumers.put(route, routable);
            consumerRoutes.put(routable, route);
        }
    }

    @Override
    public void unsubscribe(String topic, MessageConsumer routable) {
        JsonRoute route = JsonRoute.parse(topic);
        if (route != null) {
            routeConsumers.remove(route, routable);
            consumerRoutes.remove(routable, route);
        }
    }

    @Override
    public void unsubscribe(MessageConsumer routable) {
        Set<JsonRoute> routes = consumerRoutes.removeAll(routable);
        for (JsonRoute route : routes) {
            routeConsumers.remove(route, routable);
        }
    }

    @Override
    public void publish(String json) {
        EiffelMessage message = removeEiffelVersionWrapper(json);
        checkForConsumers(message);
    }

    private EiffelMessage removeEiffelVersionWrapper(String json) {
        try {
            EiffelMessageWrapper messageWrapper = objectMapper.readValue(json, EiffelMessageWrapper.class);
            return eiffelMessageService.unwrap(messageWrapper);
        } catch (IOException e) {
            logger.warn("Failed to parse Eiffel message.", e);
            return null;
        }
    }

    protected void checkForConsumers(EiffelMessage message) {
        String json = null;
        try {
            json = objectMapper.writeValueAsString(message);
        } catch (JsonProcessingException e) {
            logger.warn("Failed to emit Eiffel message.", e);
        }
        String parentId = eiffelMessageService.getParentId(message);

        for (Map.Entry<JsonRoute, Collection<MessageConsumer>> routeEntry :
                routeConsumers.asMap().entrySet()) {
            JsonRoute route = routeEntry.getKey();
            if (route.isInEventHierarchy(parentId) || route.matches(json)) {
                Collection<MessageConsumer> routables = routeEntry.getValue();
                for (MessageConsumer routable : routables) {
                    publish(routable, route, message);
                }
            }
        }
    }

    protected void publish(final MessageConsumer routable, JsonRoute route, EiffelMessage message) {
        String eventId = eiffelMessageService.getId(message);
        if (eventId != null) {
            route.addToEventHierarchy(eventId);
        }

        getRouterResponse(message, route.getTopic()).serialize(routable);
    }

    @Override
    public ServerResponseSerializer getRouterResponse(EiffelMessage eiffelMessage, String topic) {
        return ServerResponseSerializer.create(SENDER_NAME)
                .message(process(eiffelMessage))
                .data("topic", topic);
    }

    private Object process(EiffelMessage message) {
        // FIXME This is a hack to extract TAF testing event preprocessig
        // TODO Extend this to multiple processors per region type
        DashboardResponseProcessor processor = Bootstrap.getInstance().getResponseProcessor();
        if (processor != null) {
            return processor.process(message);
        } else {
            return message;
        }
    }

}
