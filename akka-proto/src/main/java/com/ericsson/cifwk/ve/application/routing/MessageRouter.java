package com.ericsson.cifwk.ve.application.routing;

import com.ericsson.cifwk.ve.application.MessageConsumer;
import com.ericsson.cifwk.ve.application.dto.EiffelMessage;
import com.ericsson.cifwk.ve.web.ServerResponseSerializer;

/**
 *
 */
public interface MessageRouter {

    void subscribe(String topic, MessageConsumer routable);

    void unsubscribe(String topic, MessageConsumer routable);

    void unsubscribe(MessageConsumer routable);

    void publish(String json);

    ServerResponseSerializer getRouterResponse(EiffelMessage eiffelMessage, String topic);

}
