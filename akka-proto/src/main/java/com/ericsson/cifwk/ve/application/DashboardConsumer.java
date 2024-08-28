package com.ericsson.cifwk.ve.application;

public class DashboardConsumer implements MessageConsumer {

    private final DashboardConnection connection;

    public DashboardConsumer(DashboardConnection connection) {
        this.connection = connection;
    }

    @Override
    public void consume(String json) {
        connection.send(json);
    }
}
