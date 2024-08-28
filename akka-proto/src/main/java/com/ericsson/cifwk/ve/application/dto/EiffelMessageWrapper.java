package com.ericsson.cifwk.ve.application.dto;

import java.util.Map;

public class EiffelMessageWrapper {

    private Map<String, EiffelMessage> eiffelMessageVersions;

    public Map<String, EiffelMessage> getEiffelMessageVersions() {
        return eiffelMessageVersions;
    }

    public void setEiffelMessageVersions(Map<String, EiffelMessage> eiffelMessageVersions) {
        this.eiffelMessageVersions = eiffelMessageVersions;
    }
}
