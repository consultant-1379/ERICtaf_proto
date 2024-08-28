package com.ericsson.cifwk.dashboard;

import com.ericsson.cifwk.dashboard.application.RabbitMqConsumer;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertTrue;

public class RabbitMqConsumerTest {

    private static final String HOST = "atclvm793.athtem.eei.ericsson.se";
    private static final int PORT = 5672;
    private static final String USERNAME = "guest";
    private static final String PASSWORD = "guest";
    private static final String EXCHANGE = "eiffel.poc.testing";

    @Test
    public void testConnect() throws IOException {
        try (RabbitMqConsumer mq = new RabbitMqConsumer(HOST, PORT, USERNAME, PASSWORD, EXCHANGE)) {
            mq.connect();
            assertTrue(mq.isOpen());
        }
    }

}
