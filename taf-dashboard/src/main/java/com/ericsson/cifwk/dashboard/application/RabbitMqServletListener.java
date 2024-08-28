package com.ericsson.cifwk.dashboard.application;

import com.ericsson.cifwk.dashboard.data.EiffelMessageData;
import com.ericsson.cifwk.dashboard.data.EiffelMessageWrapperData;
import com.ericsson.cifwk.dashboard.util.QueueManager;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.io.IOException;

@Singleton
public class RabbitMqServletListener implements ServletContextListener {

    private static final Logger log = LoggerFactory.getLogger(RabbitMqServletListener.class);

    private volatile RabbitMqConsumer mq;

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        ServletContext context = servletContextEvent.getServletContext();

        Injector injector = (Injector) context.getAttribute(Injector.class.getName());
        EiffelMessageHandler handler = new EiffelMessageHandler();
        injector.injectMembers(handler);

        String host = context.getInitParameter("amqpHost");
        Integer port = Integer.parseInt(context.getInitParameter("amqpPort"));
        String username = context.getInitParameter("amqpUsername");
        String password = context.getInitParameter("amqpPassword");
        String exchange = context.getInitParameter("amqpExchange");

        log.info("Message queue at {}:{}", host, port);
        mq = new RabbitMqConsumer(host, port, username, password, exchange);
        try {
            mq.connect();
            mq.listen(handler);
            log.info("Connected to message queue");
        } catch (IOException e) {
            log.error("Could not connect to message queue", e);
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        try {
            mq.close();
        } catch (Exception ignored) {
        }
    }

    private class EiffelMessageHandler implements RabbitMqHandler {

        @Inject
        private ObjectMapper objectMapper;

        @Inject
        private QueueManager<EiffelMessageData> queueManager;

        public EiffelMessageHandler() {
        }

        @Override
        public void handle(String routingKey, String body) {
            try {
                EiffelMessageData data = unwrapEiffelMessage(body);
                queueManager.offerToAll(data);
                log.debug("Got event from queue: {}", data.eventId);
            } catch (IOException ignored) {
            }
        }

        private EiffelMessageData unwrapEiffelMessage(String body) throws IOException {
            EiffelMessageWrapperData data = objectMapper.readValue(body, EiffelMessageWrapperData.class);
            return data.getMessage();
        }

    }
}
