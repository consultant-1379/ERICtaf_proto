package com.ericsson.cifwk.ve.web;

import com.ericsson.cifwk.ve.infrastructure.config.Settings;
import org.atmosphere.nettosphere.Config;
import org.atmosphere.nettosphere.Nettosphere;
import org.atmosphere.socketio.cpr.SocketIOAtmosphereInterceptor;

/**
 *
 */
public class WebServer {

    private Nettosphere server;
    private Settings settings;

    public WebServer(Settings settings) {
        this.settings = settings;
    }

    public void start() {
        server = new Nettosphere.Builder().config(
                new Config.Builder()
                        .host(settings.getString("netty.hostname"))
                        .port(settings.getInteger("netty.port"))
                        .initParam(SocketIOAtmosphereInterceptor.SOCKETIO_TRANSPORT, "websocket,xhr-polling,jsonp-polling")
                        .mappingPath("/api")
                        .interceptor(new SocketIOAtmosphereInterceptor())
                        .resource(DashboardDataHandler.class)
                        .resource(settings.getString("netty.webdir"))
                        .build())
                .build();

        server.start();
    }

    public void stop() {
        server.stop();
    }

}
