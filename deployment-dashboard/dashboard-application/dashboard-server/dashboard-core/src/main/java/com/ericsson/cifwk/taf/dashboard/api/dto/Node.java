package com.ericsson.cifwk.taf.dashboard.api.dto;

/**
 * Created by egergle on 10/12/2015.
 */
public class Node {

    private String type;

    private String name;

    private String status;

    private String group;

    private String system;

    private String haType;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getSystem() {
        return system;
    }

    public void setSystem(String system) {
        this.system = system;
    }

    public String getHaType() {
        return haType;
    }

    public void setHaType(String haType) {
        this.haType = haType;
    }
}
