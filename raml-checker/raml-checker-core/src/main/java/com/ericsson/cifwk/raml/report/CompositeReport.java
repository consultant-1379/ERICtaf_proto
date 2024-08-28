package com.ericsson.cifwk.raml.report;

import com.ericsson.cifwk.raml.api.Report;
import com.ericsson.cifwk.raml.api.Specification;

/**
 * @see com.ericsson.cifwk.raml.validator.CompositeValidator
 */
public class CompositeReport extends SimpleReport {

    public CompositeReport(Specification spec) {
        super(spec);
    }

    public void add(Report report) {
        report.violations().forEach(this);
    }
}
