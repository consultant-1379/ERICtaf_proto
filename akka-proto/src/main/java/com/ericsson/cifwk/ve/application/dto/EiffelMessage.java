package com.ericsson.cifwk.ve.application.dto;

import java.util.Map;

/**
 *
 */
public class EiffelMessage {

    private String domainId;
    private String eventId;
    private String eventTime;
    private String eventType;
    private String[] inputEventIds;
    private Map<String, Object> eventData;

    public String getDomainId() {
        return domainId;
    }

    public void setDomainId(String domainId) {
        this.domainId = domainId;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getEventTime() {
        return eventTime;
    }

    public void setEventTime(String eventTime) {
        this.eventTime = eventTime;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String[] getInputEventIds() {
        return inputEventIds;
    }

    public void setInputEventIds(String[] inputEventIds) {
        this.inputEventIds = inputEventIds;
    }

    public Map<String, Object> getEventData() {
        return eventData;
    }

    public void setEventData(Map<String, Object> eventData) {
        this.eventData = eventData;
    }
}
