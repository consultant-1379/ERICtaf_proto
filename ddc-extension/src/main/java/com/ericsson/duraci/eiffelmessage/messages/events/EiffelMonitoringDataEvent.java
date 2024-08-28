package com.ericsson.duraci.eiffelmessage.messages.events;

import java.util.Map;

import com.ericsson.duraci.eiffelmessage.messages.EiffelEvent;
import com.ericsson.duraci.eiffelmessage.messages.v2.events.EiffelMonitoringDataEventImpl;

public interface EiffelMonitoringDataEvent extends EiffelEvent {

    Map getData();

    String getHostName();

    static class Factory {
        public static EiffelMonitoringDataEvent create(String hostName, Map data) {
            return new EiffelMonitoringDataEventImpl(hostName, data);
        }
    }
}
