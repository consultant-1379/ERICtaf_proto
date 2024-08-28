package com.ericsson.cifwk.raml.validator;

import com.ericsson.cifwk.raml.api.Severity;
import com.ericsson.cifwk.raml.api.Violation;

import java.util.Objects;

import static com.google.common.base.MoreObjects.toStringHelper;

public class SimpleViolation implements Violation {

    private String message;
    private Severity severity;

    private SimpleViolation(String message, Severity severity) {
        this.message = message;
        this.severity = severity;
    }

    public static SimpleViolation error(String message) {
        return violation(message, Severity.ERROR);
    }

    public static SimpleViolation warning(String message) {
        return violation(message, Severity.WARNING);
    }

    public static SimpleViolation violation(String message, Severity severity) {
        return new SimpleViolation(message, severity);
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public Severity getSeverity() {
        return severity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SimpleViolation that = (SimpleViolation) o;
        return Objects.equals(message, that.message) &&
                severity == that.severity;
    }

    @Override
    public int hashCode() {
        return Objects.hash(message, severity);
    }

    @Override
    public String toString() {
        return toStringHelper(this)
                .add("message", message)
                .add("severity", severity)
                .toString();
    }
}
