package com.ericsson.cifwk.taf.graphite;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Named;
import java.io.IOException;

public class MetricsReporter {

    private final Logger logger = LoggerFactory.getLogger(MetricsReporter.class);

    private final ConnectionFactory factory;
    private final String exchange;
    private Connection connection;
    private Channel channel;

    @Inject
    public MetricsReporter(@Named("amqp.host") String host,
                           @Named("amqp.port") int port,
                           @Named("amqp.username") String username,
                           @Named("amqp.password") String password,
                           @Named("amqp.exchange") String exchange) {
        factory = new ConnectionFactory();
        factory.setHost(host);
        factory.setPort(port);
        factory.setUsername(username);
        factory.setPassword(password);
        this.exchange = exchange;
    }

    public void connect() throws IOException {
        connection = factory.newConnection();
        channel = connection.createChannel();
    }

    public void send(String name, String value, long timestamp) throws IOException {
        String message = String.format("%s %s %d\n", name, value, timestamp);
        logger.info("Reporting: {}", message.trim());
        AMQP.BasicProperties props = new AMQP.BasicProperties.Builder()
                .deliveryMode(2)
                .build();
        channel.basicPublish(exchange, "", props, message.getBytes("UTF-8"));
    }

    public void close() throws IOException {
        channel.close();
        connection.close();
    }

}
