package com.ericsson.cifwk.raml.validator.check;

import com.ericsson.cifwk.raml.api.Severity;
import com.ericsson.cifwk.raml.api.Violation;

import java.util.Optional;
import java.util.stream.Stream;

import static com.ericsson.cifwk.raml.validator.SimpleViolation.violation;
import static java.util.Optional.empty;
import static java.util.Optional.of;

public abstract class FluentCheck<ENTITY, CHECK extends FluentCheck<ENTITY, CHECK>> {

    private final ENTITY entity;
    private boolean condition;
    private StringBuilder message;

    FluentCheck(ENTITY entity) {
        this.entity = entity;
        this.condition = true;
        as(representation(entity));
    }

    protected String representation(ENTITY entity) {
        return String.valueOf(entity);
    }

    public CHECK as(String representation) {
        message = new StringBuilder(representation.trim() + " ");
        return me();
    }

    CHECK meets(boolean condition, String... messageParts) {
        this.condition &= condition;
        if (this.condition) {
            Stream.of(messageParts).forEach(message::append);
        }
        return me();
    }

    public CHECK but() {
        message.append(", but ");
        return me();
    }

    @SuppressWarnings("unchecked")
    private CHECK me() {
        return (CHECK) this;
    }

    ENTITY entity() {
        return entity;
    }

    public Optional<Violation> then(Severity severity) {
        return then(severity, message.toString());
    }

    public Optional<Violation> then(Severity severity, String message) {
        if (condition) {
            return of(violation(message, severity));
        } else {
            return empty();
        }
    }
}
