package com.ericsson.cifwk.taf.grid;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.javasimon.SimonManager;
import org.javasimon.Split;
import org.javasimon.Stopwatch;
import org.javasimon.console.SimonConsoleServlet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

/**
 *
 */
public final class EmbeddedJetty {

    private static final Logger logger = LoggerFactory.getLogger(EmbeddedJetty.class);

    public static void main(String[] args) throws Exception {
        Server server = new Server(9090);
        ServletContextHandler servletHandler = new ServletContextHandler();
        SimonConsoleServlet servlet = new SimonConsoleServlet();
        ServletHolder holder = new ServletHolder(servlet);
        holder.setName("simon");
        holder.setEnabled(true);
        holder.setInitParameter("url-prefix", "/simon");
        servletHandler.addServlet(holder, "/simon/*");

        ServletHolder testServletHolder = new ServletHolder(new TestServlet());
        servletHandler.addServlet(testServletHolder, "/test");

        ContextHandler contextHandler = new ContextHandler();
        contextHandler.setContextPath("/");
        contextHandler.setHandler(servletHandler);
        server.setHandler(contextHandler);

        try {
            server.start();
        } catch (Exception e) {
            server.stop();
            throw e;
        }
    }

    public static class TestServlet extends HttpServlet {

        Stopwatch stopwatch = SimonManager.getStopwatch("http.get");

        @Override
        protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
            Split split = stopwatch.start();
            String timestamp = new Date().toString();
            logger.info("Request received : {}", timestamp);
            resp.getWriter().write(timestamp);
            resp.setStatus(200);
            split.stop();
        }

    }

}
