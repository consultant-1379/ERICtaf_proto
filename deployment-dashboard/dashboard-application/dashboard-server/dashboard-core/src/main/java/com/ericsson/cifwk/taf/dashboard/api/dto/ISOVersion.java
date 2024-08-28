package com.ericsson.cifwk.taf.dashboard.api.dto;

/**
 * Created by egergle on 10/12/2015.
 */
public class ISOVersion {

    public static final String TYPE = "version";

    private String version;

    private String type;

    public String getVersion() {
        return version;
    }

    public void setVersion(String name) {
        this.version = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
