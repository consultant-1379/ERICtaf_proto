package com.ericsson.cifwk.dashboard.data;

import java.util.Map;

public class EiffelMessageWrapperData {

    public Map<String, EiffelMessageData> eiffelMessageVersions;

    public EiffelMessageData getMessage() {
        return eiffelMessageVersions.values().iterator().next();
    }

}
