package com.ericsson.cifwk.taf.grid.shared;

import java.io.Serializable;
import java.util.Map;
import java.util.UUID;

/**
 *
 */
public final class TestStep implements Serializable {

    private UUID id = UUID.randomUUID();
    private String[] members;
    private TestSchedule schedule;
    private Map<String, String> attributes;

    public TestStep() {
    }

    public TestStep(String[] members, TestSchedule schedule, Map<String, String> attributes) {
        this.members = members;
        this.schedule = schedule;
        this.attributes = attributes;
    }

    public UUID getId() {
        return id;
    }

    public String[] getMembers() {
        return members;
    }

    public TestSchedule getSchedule() {
        return schedule;
    }

    public Map<String, String> getAttributes() {
        return attributes;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public void setMembers(String[] members) {
        this.members = members;
    }

    public void setSchedule(TestSchedule schedule) {
        this.schedule = schedule;
    }

    public void setAttributes(Map<String, String> attributes) {
        this.attributes = attributes;
    }
}
