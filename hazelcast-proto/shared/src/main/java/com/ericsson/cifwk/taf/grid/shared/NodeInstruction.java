package com.ericsson.cifwk.taf.grid.shared;

import java.io.Serializable;
import java.util.Map;
import java.util.UUID;

/**
 *
 */
public class NodeInstruction implements Serializable {

    private UUID id;
    private String clientId;
    private String runner;
    private String container;
    private String classpath;
    private TestSchedule schedule;
    private Map<String, String> attributes;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getContainer() {
        return container;
    }

    public void setContainer(String container) {
        this.container = container;
    }

    public String getRunner() {
        return runner;
    }

    public void setRunner(String runner) {
        this.runner = runner;
    }

    public String getClasspath() {
        return classpath;
    }

    public void setClasspath(String classpath) {
        this.classpath = classpath;
    }

    public TestSchedule getSchedule() {
        return schedule;
    }

    public void setSchedule(TestSchedule schedule) {
        this.schedule = schedule;
    }

    public Map<String, String> getAttributes() {
        return attributes;
    }

    public void setAttributes(Map<String, String> attributes) {
        this.attributes = attributes;
    }
}
