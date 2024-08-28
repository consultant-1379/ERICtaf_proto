package com.ericsson.cifwk.raml.api;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

import static java.util.Comparator.naturalOrder;

public interface Report extends Consumer<Violation> {

    String name();

    List<Violation> violations();

    default boolean hasViolations() {
        return !violations().isEmpty();
    }

    default Optional<Severity> severity() {
        return violations().stream()
                .map(Violation::getSeverity)
                .max(naturalOrder());
    }
}
