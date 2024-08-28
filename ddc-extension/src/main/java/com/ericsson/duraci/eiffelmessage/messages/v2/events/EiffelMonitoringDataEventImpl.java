package com.ericsson.duraci.eiffelmessage.messages.v2.events;

import java.util.Map;

import com.ericsson.duraci.eiffelmessage.messages.Event;
import com.ericsson.duraci.eiffelmessage.messages.events.EiffelMonitoringDataEvent;
import com.ericsson.duraci.eiffelmessage.messages.v2.events.virtual.GenericEiffelEventFieldsImpl;

@Event(eventType = "EiffelMonitoringDataEvent")
public class EiffelMonitoringDataEventImpl extends GenericEiffelEventFieldsImpl implements EiffelMonitoringDataEvent {

    private final Map data;

    private final String hostName;

    public EiffelMonitoringDataEventImpl(EiffelMonitoringDataEvent source) {
        setGenericFields(source);
        data = source.getData();
        hostName = source.getHostName();
    }

    public EiffelMonitoringDataEventImpl(String hostName, Map data) {
        this.hostName = hostName;
        this.data = data;
    }

    public String getRoutingKeySuffix() {
        return "monitoring";
    }

    public Map getData() {
        return data;
    }

    public String getHostName() {
        return hostName;
    }

}
