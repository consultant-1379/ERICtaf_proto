package com.ericsson.cifwk.model;

import java.util.StringJoiner;

import static java.lang.String.valueOf;

public class Component implements SerializableToCsv {

    private final String componentId;
    private final String name;
    private final String version;
    private final String primId;
    private final Stako stako;
    private final String license;

    public Component(String componentId,
                     String name, String version,
                     String primId, Stako stako,
                     String license) {
        this.componentId = componentId;
        this.name = name;
        this.version = version;
        this.primId = primId;
        this.stako = stako;
        this.license = license;
    }

    public String getComponentId() {
        return componentId;
    }

    public String getName() {
        return name;
    }

    public String getVersion() {
        return version;
    }

    public String getPrimId() {
        return primId;
    }

    public Stako getStako() {
        return stako;
    }

    public String getLicense() {
        return license;
    }

    @Override
    public String toCsvRow() {
        StringJoiner row = new StringJoiner(",");
        row.add(quotes(componentId));
        row.add(quotes(name));
        row.add(quotes(version));
        row.add(quotes(primId));
        row.add(quotes(valueOf(stako)));
        row.add(quotes(license));
        return row.toString();
    }

    @Override
    public String toString() {
        return "Component{" +
                "componentId='" + componentId + '\'' +
                ", name='" + name + '\'' +
                ", version='" + version + '\'' +
                ", primId='" + primId + '\'' +
                ", stako=" + stako +
                ", license='" + license + '\'' +
                '}';
    }
}
