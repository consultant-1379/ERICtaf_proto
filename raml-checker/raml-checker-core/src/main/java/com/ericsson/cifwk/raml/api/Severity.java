package com.ericsson.cifwk.raml.api;

public enum Severity {
    WARNING, ERROR;

    public boolean isGreaterThanOrEqualTo(Severity severity) {
        return compareTo(severity) >= 0;
    }
}
