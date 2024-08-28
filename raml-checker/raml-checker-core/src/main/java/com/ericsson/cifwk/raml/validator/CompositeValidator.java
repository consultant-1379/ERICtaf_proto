package com.ericsson.cifwk.raml.validator;

import com.ericsson.cifwk.raml.api.Report;
import com.ericsson.cifwk.raml.api.Specification;
import com.ericsson.cifwk.raml.api.Validator;
import com.ericsson.cifwk.raml.report.CompositeReport;

import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.collect.Lists.newArrayList;

public class CompositeValidator implements Validator {

    private List<Validator> validators;

    public CompositeValidator(Validator... validators) {
        checkArgument(validators.length > 0);
        this.validators = newArrayList(validators);
    }

    @Override
    public Report validate(Specification spec) {
        CompositeReport report = new CompositeReport(spec);
        for (Validator validator : validators) {
            report.add(validator.validate(spec));
        }
        return report;
    }
}
