package com.ericsson.cifwk.raml.validator;

import com.ericsson.cifwk.raml.api.Report;
import org.junit.Test;

public class TraitEnforcingValidatorTest extends ValidatorTest {

    private TraitEnforcingValidator validator = new TraitEnforcingValidator();

    @Test
    public void errorHandler_invalid() throws Exception {
        Report report = run(validator, "traits/error-handler-invalid.raml");

        assertHasViolations(report, "action(GET /resource) has response code 500, but does not have trait 'error-handler'");
    }

    @Test
    public void errorHandler_valid() throws Exception {
        Report report = run(validator, "traits/error-handler-valid.raml");

        assertNoViolations(report);
    }

    @Test
    public void filterable_invalid() throws Exception {
        Report report = run(validator, "traits/filterable-invalid.raml");

        assertHasViolations(report, "action(GET /resource) has query parameter 'filter', but does not have trait 'filterable'");
    }

    @Test
    public void filterable_valid() throws Exception {
        Report report = run(validator, "traits/filterable-valid.raml");

        assertNoViolations(report);
    }

    @Test
    public void pageable_invalid() throws Exception {
        Report report = run(validator, "traits/pageable-invalid.raml");

        assertHasViolations(report, "action(GET /resource) has query parameters 'offset'/'limit'/'page', but does not have trait 'pageable'");
    }

    @Test
    public void pageable_valid() throws Exception {
        Report report = run(validator, "traits/pageable-valid.raml");

        assertNoViolations(report);
    }

    @Test
    public void projectable_invalid() throws Exception {
        Report report = run(validator, "traits/projectable-invalid.raml");

        assertHasViolations(report, "action(GET /resource) has query parameter 'select', but does not have trait 'projectable'");
    }

    @Test
    public void projectable_valid() throws Exception {
        Report report = run(validator, "traits/projectable-valid.raml");

        assertNoViolations(report);
    }

    @Test
    public void searchable_invalid() throws Exception {
        Report report = run(validator, "traits/searchable-invalid.raml");

        assertHasViolations(report, "action(GET /resource) has query parameters 'search'/'find', but does not have trait 'searchable'");
    }

    @Test
    public void searchable_valid() throws Exception {
        Report report = run(validator, "traits/searchable-valid.raml");

        assertNoViolations(report);
    }

    @Test
    public void sortable_invalid() throws Exception {
        Report report = run(validator, "traits/sortable-invalid.raml");

        assertHasViolations(report,
                "action(GET /resource-a) has query parameters 'orderBy'/'sort', but does not have trait 'sortable'",
                "action(GET /resource-b) has query parameters 'order'/'sorting', but does not have trait 'sortable'"
        );
    }

    @Test
    public void sortable_valid() throws Exception {
        Report report = run(validator, "traits/sortable-valid.raml");

        assertNoViolations(report);
    }

    @Test
    public void validatable_invalid() throws Exception {
        Report report = run(validator, "traits/validatable-invalid.raml");

        assertHasViolations(report, "action(GET /resource) has response code 422, but does not have trait 'validatable'");
    }

    @Test
    public void validatable_valid() throws Exception {
        Report report = run(validator, "traits/validatable-valid.raml");

        assertNoViolations(report);
    }

    @Test
    public void all_traits_invalid() throws Exception {
        Report report = run(validator, "traits/all-traits-invalid.raml");

        assertHasViolations(report,
                "action(GET /resource-a) has response code 500, but does not have trait 'error-handler'",
                "action(GET /resource-a/sub-resource-b) has query parameter 'filter', but does not have trait 'filterable'",
                "action(GET /resource-a/sub-resource-c) has query parameters 'offset'/'limit'/'page', but does not have trait 'pageable'",
                "action(GET /resource-d) has query parameter 'select', but does not have trait 'projectable'",
                "action(GET /resource-d/sub-resource-e) has query parameters 'search'/'find', but does not have trait 'searchable'",
                "action(GET /resource-d/sub-resource-f) has query parameters 'orderBy'/'sort', but does not have trait 'sortable'",
                "action(GET /resource-d/sub-resource-f/sub-resource-g) has query parameters 'order'/'sorting', but does not have trait 'sortable'",
                "action(GET /resource-d/sub-resource-f/sub-resource-h) has response code 422, but does not have trait 'validatable'"
        );
    }
}
