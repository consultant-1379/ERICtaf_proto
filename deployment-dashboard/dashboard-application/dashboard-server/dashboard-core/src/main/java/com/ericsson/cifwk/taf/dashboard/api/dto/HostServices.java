package com.ericsson.cifwk.taf.dashboard.api.dto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by egergle on 10/12/2015.
 */
public class HostServices {

    public static final String TYPE = "hostServices";

    private String type;

    private Map<String, String> services = new HashMap<>();

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Map<String, String> getServices() {
        return services;
    }

    public void setServices(Map<String, String> services) {
        this.services = services;
    }
}
