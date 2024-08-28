package com.ericsson.cifwk.taf.dashboard.api.dto;

import com.ericsson.cifwk.taf.dashboard.api.dto.Node;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by egergle on 10/12/2015.
 */
public class NodeServices {

    public static final String TYPE = "nodeServices";

    private String type;

    private List<Node> services = new ArrayList<>();

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<Node> getServices() {
        return services;
    }

    public void setServices(List<Node> services) {
        this.services = services;
    }
}
