package com.ericsson.cifwk.raml.validator;

import com.ericsson.cifwk.raml.RamlTest;
import com.ericsson.cifwk.raml.api.Report;
import com.ericsson.cifwk.raml.api.Specification;
import com.ericsson.cifwk.raml.api.Validator;
import com.ericsson.cifwk.raml.api.Violation;
import com.ericsson.cifwk.raml.spec.SimpleSpecification;
import guru.nidi.ramltester.RamlDefinition;

import static org.assertj.core.api.Assertions.assertThat;

abstract class ValidatorTest extends RamlTest {

    Report run(Validator validator, String file) {
        RamlDefinition definition = load(file);
        Specification spec = new SimpleSpecification("test", definition);
        return validator.validate(spec);
    }

    void assertNoViolations(Report report) {
        assertThat(report.violations()).isEmpty();
    }

    void assertHasViolations(Report report, String... violations) {
        assertThat(report.violations())
                .isNotEmpty()
                .extracting(Violation::getMessage)
                .containsExactly(violations);
    }
}
