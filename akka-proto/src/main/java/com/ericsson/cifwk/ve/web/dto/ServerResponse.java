package com.ericsson.cifwk.ve.web.dto;

import java.util.Map;

public class ServerResponse {

    private String sender;
    private Object message;
    private Map serverData;

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public Object getMessage() {
        return message;
    }

    public void setMessage(Object message) {
        this.message = message;
    }

    public Map getServerData() {
        return serverData;
    }

    public void setServerData(Map serverData) {
        this.serverData = serverData;
    }
}
