package com.ericsson.cifwk.ve.web;

import com.ericsson.cifwk.ve.application.Bootstrap;
import com.ericsson.cifwk.ve.plugins.DashboardMessageHandler;
import com.ericsson.cifwk.ve.web.dto.ClientMessage;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Throwables;
import org.atmosphere.config.service.AtmosphereHandlerService;
import org.atmosphere.cpr.AtmosphereResource;
import org.atmosphere.interceptor.AtmosphereResourceLifecycleInterceptor;
import org.atmosphere.socketio.SocketIOSessionOutbound;
import org.atmosphere.socketio.cpr.SocketIOAtmosphereHandler;
import org.atmosphere.socketio.transport.DisconnectReason;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

/**
 *
 */
@AtmosphereHandlerService(path = "/api", interceptors = {AtmosphereResourceLifecycleInterceptor.class})
public class DashboardDataHandler extends SocketIOAtmosphereHandler {

    final Logger logger = LoggerFactory.getLogger(DashboardDataHandler.class.getName());

    @Override
    public void onConnect(AtmosphereResource event, SocketIOSessionOutbound handler) throws IOException {
        String sessionId = handler.getSessionId();
        DashboardService dashboardService = Bootstrap.getInstance().getDashboardService();
        dashboardService.connect(sessionId, new AtmosphereConnection(handler));
        logger.info("Connected: " + sessionId);
    }

    @Override
    public void onDisconnect(AtmosphereResource event, SocketIOSessionOutbound handler, DisconnectReason reason) {
        String sessionId = handler.getSessionId();
        DashboardService dashboardService = Bootstrap.getInstance().getDashboardService();
        dashboardService.disconnect(sessionId);
        logger.info("Disconnected: " + sessionId);
    }

    @Override
    public void onMessage(AtmosphereResource event, SocketIOSessionOutbound handler, String message) {
        ObjectMapper objectMapper = Bootstrap.getInstance().getObjectMapper();
        ClientMessage clientMessage = parseJson(message, objectMapper);
        String messageName = clientMessage.getName();
        if (messageName != null) {
            String sessionId = handler.getSessionId();
            Map args = clientMessage.getActualArgs();
            Set<DashboardMessageHandler> messageHandlers = Bootstrap.getInstance().getMessageHandlers(messageName);
            for (DashboardMessageHandler messageHandler : messageHandlers) {
                messageHandler.handle(sessionId, args);
            }
        } else {
            logger.warn("No topic handler for message: " + message);
        }
    }

    private ClientMessage parseJson(String message, ObjectMapper objectMapper) {
        try {
            return objectMapper.readValue(message, ClientMessage.class);
        } catch (IOException e) {
            throw Throwables.propagate(e);
        }
    }

    @Override
    public void destroy() {
    }

}
