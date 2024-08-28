package com.ericsson.cifwk.ve.plugins.amqp;

import akka.camel.CamelMessage;
import akka.camel.javaapi.UntypedConsumerActor;
import com.ericsson.cifwk.ve.application.Bootstrap;
import com.ericsson.cifwk.ve.web.DashboardService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 */
public class AmqpSourceActor extends UntypedConsumerActor {

    final Logger logger = LoggerFactory.getLogger(AmqpSourceActor.class.getName());

    private final String host;
    private final Integer port;
    private final String exchangeName;
    private final String username;
    private final String password;
    private final String routingKey;

    public AmqpSourceActor(String host,
                           Integer port,
                           String exchangeName,
                           String username,
                           String password,
                           String routingKey) {
        this.host = host;
        this.port = port;
        this.exchangeName = exchangeName;
        this.username = username;
        this.password = password;
        this.routingKey = routingKey;
    }

    @Override
    public String getEndpointUri() {
        return String.format("rabbitmq://%s:%s/%s?username=%s&password=%s&durable=false&autoDelete=false&routingKey=%s",
                host, port, exchangeName, username, password, routingKey);
    }

    @Override
    public void onReceive(Object message) throws Exception {
        logger.info("AMQP event received");
        if (message instanceof CamelMessage) {
            CamelMessage camelMessage = (CamelMessage) message;
            handle(camelMessage);
        } else {
            unhandled(message);
        }
    }

    private void handle(CamelMessage message) {
        DashboardService dashboardService = Bootstrap.getInstance().getDashboardService();
        String jsonMessage = message.getBodyAs(String.class, getCamelContext());
        logger.debug(jsonMessage);
        dashboardService.publish(jsonMessage);
    }

}
