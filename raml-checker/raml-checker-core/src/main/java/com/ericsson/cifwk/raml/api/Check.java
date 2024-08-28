package com.ericsson.cifwk.raml.api;

import java.util.Optional;

/**
 * Functional interface encapsulating single validation of a part of a RAML model
 */
@FunctionalInterface
public interface Check<T> {

    /**
     * Executes a single specific check on a part of a RAML model
     *
     * @param model Part of a RAML model for validation,
     *              specifically, {@link org.raml.model.Raml},
     *              {@link org.raml.model.Resource} or
     *              {@link org.raml.model.Action}
     * @return {@code Optional.empty()} if check passed,
     * and {@code Optional.of(<Violation>)} otherwise
     */
    Optional<Violation> check(T model);

}
