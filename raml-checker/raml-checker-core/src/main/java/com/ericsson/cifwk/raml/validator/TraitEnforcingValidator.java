package com.ericsson.cifwk.raml.validator;

import com.ericsson.cifwk.raml.api.Report;
import com.ericsson.cifwk.raml.api.Violation;
import org.raml.model.Action;

import java.util.Optional;

import static com.ericsson.cifwk.raml.api.Severity.WARNING;
import static com.ericsson.cifwk.raml.validator.Validations.failAll;
import static com.ericsson.cifwk.raml.validator.check.Checks.when;

public class TraitEnforcingValidator extends SimpleValidator {

    @Override
    protected void validateAction(Action action, Report report) {
        failAll(action, report,
                this::errorHandler,
                this::filterable,
                this::pageable,
                this::projectable,
                this::searchable,
                this::sortable,
                this::validatable
        );
    }

    private Optional<Violation> errorHandler(Action action) {
        return when(action).hasResponseCode("500").but().hasNoTrait("error-handler").then(WARNING);
    }

    private Optional<Violation> filterable(Action action) {
        return when(action).hasQueryParam("filter").but().hasNoTrait("filterable").then(WARNING);
    }

    private Optional<Violation> pageable(Action action) {
        return when(action).hasOneOfQueryParams("offset", "limit", "page").but().hasNoTrait("pageable").then(WARNING);
    }

    private Optional<Violation> projectable(Action action) {
        return when(action).hasQueryParam("select").but().hasNoTrait("projectable").then(WARNING);
    }

    private Optional<Violation> searchable(Action action) {
        return when(action).hasOneOfQueryParams("search", "find").but().hasNoTrait("searchable").then(WARNING);
    }

    private Optional<Violation> sortable(Action action) {
        return when(action).hasOneOfQueryParams("orderBy", "order", "sort", "sorting").but().hasNoTrait("sortable").then(WARNING);
    }

    private Optional<Violation> validatable(Action action) {
        return when(action).hasResponseCode("422").but().hasNoTrait("validatable").then(WARNING);
    }
}
