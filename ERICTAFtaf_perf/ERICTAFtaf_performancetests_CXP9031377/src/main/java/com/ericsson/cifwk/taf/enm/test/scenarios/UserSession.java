package com.ericsson.cifwk.taf.enm.test.scenarios;

import com.ericsson.cifwk.taf.tools.http.HttpTool;

/**
 *
 */
public class UserSession {

    private final HttpTool tool;

    public UserSession(HttpTool tool) {
        this.tool = tool;
    }

    public HttpTool getTool() {
        return tool;
    }
}
