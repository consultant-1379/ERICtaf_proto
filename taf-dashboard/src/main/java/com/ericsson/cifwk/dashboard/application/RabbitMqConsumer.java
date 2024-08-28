
package com.ericsson.cifwk.dashboard.application;

import com.rabbitmq.client.*;

import java.io.Closeable;
import java.io.IOException;

public class RabbitMqConsumer implements Closeable {

    private static final String DEFAULT_ROUTING_KEY = "#";

    private final String host;
    private final int port;
    private final String username;
    private final String password;
    private final String exchange;

    private Connection connection;
    private Channel channel;

    public RabbitMqConsumer(
            String host,
            int port,
            String username,
            String password,
            String exchange) {
        this.host = host;
        this.port = port;
        this.username = username;
        this.password = password;
        this.exchange = exchange;
    }

    public void connect() throws IllegalStateException, IOException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(host);
        factory.setPort(port);
        factory.setUsername(username);
        factory.setPassword(password);
        connection = factory.newConnection();
        channel = connection.createChannel();
        channel.exchangeDeclare(exchange, "topic");
    }

    public boolean isOpen() {
        return channel != null && channel.isOpen();
    }

    public void listen(final RabbitMqHandler handler)
            throws IOException {
        String queueName = channel.queueDeclare().getQueue();
        channel.queueBind(queueName, exchange, DEFAULT_ROUTING_KEY);
        channel.basicConsume(queueName, true, new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag,
                                       Envelope envelope,
                                       AMQP.BasicProperties properties,
                                       byte[] body)
                    throws IOException {
                String routingKey = envelope.getRoutingKey();
                String stringBody = new String(body);
                handler.handle(routingKey, stringBody);
            }
        });
    }

    @Override
    public void close() throws IOException {
        channel.close();
        connection.close();
    }

}
