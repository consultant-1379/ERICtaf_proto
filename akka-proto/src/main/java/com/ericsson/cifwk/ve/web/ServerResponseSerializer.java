package com.ericsson.cifwk.ve.web;

import com.ericsson.cifwk.ve.application.Bootstrap;
import com.ericsson.cifwk.ve.application.MessageConsumer;
import com.ericsson.cifwk.ve.web.dto.ServerResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.HashMap;

public class ServerResponseSerializer {

    private final ServerResponse response;
    private final HashMap<String, Object> serverData;

    private ServerResponseSerializer() {
        response = new ServerResponse();
        serverData = new HashMap<>();
        response.setServerData(serverData);
    }

    public static ServerResponseSerializer create(String sender) {
        ServerResponseSerializer serializer = new ServerResponseSerializer();
        serializer.response.setSender(sender);
        return serializer;
    }

    public ServerResponseSerializer message(Object message) {
        response.setMessage(message);
        return this;
    }

    public ServerResponseSerializer data(String name, Object data) {
        serverData.put(name, data);
        return this;
    }

    public void serialize(MessageConsumer consumer) {
        if (response.getMessage() == null) {
            return;
        }
        ObjectMapper objectMapper = Bootstrap.getInstance().getObjectMapper();
        try {
            String json = objectMapper.writeValueAsString(response);
            consumer.consume(json);
        } catch (JsonProcessingException ignored) {
        }
    }

}
