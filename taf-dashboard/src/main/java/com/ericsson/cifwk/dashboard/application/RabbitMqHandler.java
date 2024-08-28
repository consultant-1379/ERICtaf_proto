package com.ericsson.cifwk.dashboard.application;

public interface RabbitMqHandler {

    void handle(String routingKey, String body);

}
