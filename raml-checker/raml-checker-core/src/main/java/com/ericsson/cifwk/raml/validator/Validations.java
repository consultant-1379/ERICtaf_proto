package com.ericsson.cifwk.raml.validator;

import com.ericsson.cifwk.raml.api.Check;
import com.ericsson.cifwk.raml.api.Report;
import com.ericsson.cifwk.raml.api.Violation;

import java.util.Optional;
import java.util.stream.Stream;

import static com.google.common.base.Preconditions.checkArgument;

final class Validations {

    private Validations() {
    }

    /**
     * Run all provided {@code checks} and report only the first violation
     *
     * @param model  Part of a RAML model for validation
     * @param checks {@code Check} instances to run on {@code model}
     * @see Check
     */
    @SafeVarargs
    static <T> void failFast(T model, Report report, Check<T>... checks) {
        run(model, checks)
                .findFirst()
                .ifPresent(report);
    }

    /**
     * Run all provided {@code checks} and report all violations
     *
     * @param model  Part of a RAML model for validation
     * @param checks {@code Check} instances to run on {@code model}
     * @see Check
     */
    @SafeVarargs
    static <T> void failAll(T model, Report report, Check<T>... checks) {
        run(model, checks).forEach(report);
    }

    @SafeVarargs
    private static <T> Stream<Violation> run(T model, Check<T>... checks) {
        checkArgument(checks.length > 0);
        return Stream.of(checks)
                .map(check -> check.check(model))
                .filter(Optional::isPresent)
                .map(Optional::get);
    }
}
