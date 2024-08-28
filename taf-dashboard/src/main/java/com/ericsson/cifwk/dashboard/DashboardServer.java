package com.ericsson.cifwk.dashboard;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;

public final class DashboardServer {

    private static final Logger log = LoggerFactory.getLogger(DashboardServer.class);

    private final Server server;

    public DashboardServer(int port, String webDir) {
        log.info("Initializing server in {}", webDir);

        server = new Server(port);
        WebAppContext webapp = new WebAppContext();
        webapp.setContextPath("/");
        webapp.setResourceBase(webDir);
        webapp.setParentLoaderPriority(true);
        server.setHandler(webapp);
    }

    public void start() throws Exception {
        log.info("Starting server");
        server.start();
    }

    public void stop() throws Exception {
        server.stop();
    }

    public static void main(String[] args) throws Exception {
        ClassLoader classLoader = DashboardServer.class.getClassLoader();
        URL resource = classLoader.getResource("static");
        String webDir = resource.toExternalForm();

        int port = Integer.parseInt(System.getProperty("port", "3000"));
        DashboardServer server = new DashboardServer(port, webDir);
        server.start();
    }

}
