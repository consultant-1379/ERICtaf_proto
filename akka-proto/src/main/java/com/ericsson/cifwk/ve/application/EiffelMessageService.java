package com.ericsson.cifwk.ve.application;

import com.ericsson.cifwk.ve.application.dto.EiffelMessage;
import com.ericsson.cifwk.ve.application.dto.EiffelMessageWrapper;

import java.util.HashMap;
import java.util.Map;

public class EiffelMessageService {

    private final String preferredVersion;

    public EiffelMessageService(String preferredVersion) {
        this.preferredVersion = preferredVersion;
    }

    public EiffelMessageWrapper wrap(EiffelMessage message) {
        return wrap(message, preferredVersion);
    }

    public EiffelMessageWrapper wrap(EiffelMessage message, String version) {
        HashMap<String, EiffelMessage> eiffelMessageVersions = new HashMap<>();
        eiffelMessageVersions.put(version, message);
        EiffelMessageWrapper wrapper = new EiffelMessageWrapper();
        wrapper.setEiffelMessageVersions(eiffelMessageVersions);
        return wrapper;
    }

    public EiffelMessage unwrap(EiffelMessageWrapper wrapper) {
        return unwrap(wrapper, preferredVersion);
    }

    public EiffelMessage unwrap(EiffelMessageWrapper wrapper, String version) {
        Map<String, EiffelMessage> eiffelMessageVersions = wrapper.getEiffelMessageVersions();
        EiffelMessage message = eiffelMessageVersions.get(version);
        if (message != null) {
            return message;
        }
        return eiffelMessageVersions.values().iterator().next();
    }

    public String getId(EiffelMessage message) {
        return (String) message.getEventData().get("eventId");
    }

    public void setId(EiffelMessage message, String id) {
        if (id != null) {
            message.getEventData().put("eventId", id);
        }
    }

    public String getParentId(EiffelMessage message) {
        return (String) message.getEventData().get("parentId");
    }

    public void setParentId(EiffelMessage message, String parentId) {
        if (parentId != null) {
            message.getEventData().put("parentId", parentId);
        }
    }

}
