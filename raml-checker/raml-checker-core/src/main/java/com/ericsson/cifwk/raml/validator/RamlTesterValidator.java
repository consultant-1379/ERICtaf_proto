package com.ericsson.cifwk.raml.validator;

import com.ericsson.cifwk.raml.api.Report;
import com.ericsson.cifwk.raml.api.Specification;
import com.ericsson.cifwk.raml.api.Validator;
import com.ericsson.cifwk.raml.report.SimpleReport;
import guru.nidi.ramltester.core.RamlValidator;
import guru.nidi.ramltester.core.RamlViolationMessage;
import guru.nidi.ramltester.core.RamlViolations;
import guru.nidi.ramltester.core.Validation;

import java.util.EnumSet;
import java.util.Set;

import static com.google.common.collect.Sets.newHashSet;
import static java.util.stream.StreamSupport.stream;

/**
 * Runs {@code raml-tester} RAML specification
 * validations excluding mandatory description
 *
 * @see RamlValidator
 */
public class RamlTesterValidator implements Validator {

    static final String RESOURCE_REGEX = "[a-z\\-]+";

    @Override
    public Report validate(Specification spec) {
        RamlValidator validator = spec.getDefinition().validator()
                .withChecks(excluding(Validation.DESCRIPTION))
                .withResourcePattern(RESOURCE_REGEX);
        RamlViolations violations = validator.validate().getValidationViolations();
        return createReport(spec, violations);
    }

    private Validation[] excluding(Validation... excludes) {
        Set<Validation> validations = EnumSet.allOf(Validation.class);
        validations.removeAll(newHashSet(excludes));
        return validations.toArray(new Validation[validations.size()]);
    }

    private SimpleReport createReport(Specification spec, RamlViolations violations) {
        SimpleReport report = new SimpleReport(spec);
        stream(violations.spliterator(), false)
                .map(RamlViolationMessage::getMessage)
                .map(SimpleViolation::error)
                .forEach(report);
        return report;
    }
}
