package com.ericsson.cifwk.taf.dashboard.api.dto;

/**
 * Created by egergle on 10/12/2015.
 */
public class DeploymentRequest {

    private String clusterId;

    private String uid;

    public String getClusterId() {
        return clusterId;
    }

    public void setClusterId(String clusterId) {
        this.clusterId = clusterId;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
