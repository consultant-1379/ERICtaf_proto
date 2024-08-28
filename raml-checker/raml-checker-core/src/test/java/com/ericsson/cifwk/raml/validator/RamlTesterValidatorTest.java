package com.ericsson.cifwk.raml.validator;

import com.ericsson.cifwk.raml.api.Report;
import org.junit.Test;

import static com.ericsson.cifwk.raml.validator.RamlTesterValidator.RESOURCE_REGEX;

public class RamlTesterValidatorTest extends ValidatorTest {

    private RamlTesterValidator validator = new RamlTesterValidator();

    @Test
    public void resourcePattern_invalid() throws Exception {
        Report report = run(validator, "tester/resource-pattern-invalid.raml");

        assertHasViolations(report,
                "Name of resource(/CAPITAL-LETTERS) does not match pattern '" + RESOURCE_REGEX +  "'",
                "Name of resource(/has-digit-1) does not match pattern '" + RESOURCE_REGEX +  "'",
                "Name of resource(/has_non_dash_symbols) does not match pattern '" + RESOURCE_REGEX +  "'"
        );
    }

    @Test
    public void resourcePattern_valid() throws Exception {
        Report report = run(validator, "tester/resource-pattern-valid.raml");

        assertNoViolations(report);
    }
}
