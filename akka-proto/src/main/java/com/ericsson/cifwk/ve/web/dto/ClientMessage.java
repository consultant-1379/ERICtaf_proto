package com.ericsson.cifwk.ve.web.dto;

import java.util.Collections;
import java.util.Map;

/**
 *
 */
public class ClientMessage {

    private String name;
    private Map[] args;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map[] getArgs() {
        return args;
    }

    public void setArgs(Map[] args) {
        this.args = args;
    }

    public Map getActualArgs() {
        if (args.length > 0) {
            return args[0];
        } else {
            return Collections.emptyMap();
        }
    }

}
