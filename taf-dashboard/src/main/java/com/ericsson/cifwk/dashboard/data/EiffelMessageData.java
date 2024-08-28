package com.ericsson.cifwk.dashboard.data;

import java.util.Arrays;
import java.util.Date;
import java.util.Map;
import java.util.Objects;

public class EiffelMessageData {

    public String domainId;
    public String eventId;
    public Date eventTime;
    public String eventType;
    public String[] inputEventIds;
    public Map<String, Object> eventData;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        EiffelMessageData that = (EiffelMessageData) o;
        return Objects.equals(domainId, that.domainId) &&
                Objects.equals(eventId, that.eventId) &&
                Objects.equals(eventTime, that.eventTime) &&
                Objects.equals(eventType, that.eventType) &&
                Arrays.equals(inputEventIds, that.inputEventIds) &&
                Objects.equals(eventData, that.eventData);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                domainId,
                eventId,
                eventTime,
                eventType,
                inputEventIds,
                eventData
        );
    }
}
