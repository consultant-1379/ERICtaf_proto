package com.ericsson.cifwk.raml.report;

import com.ericsson.cifwk.raml.api.Report;
import com.ericsson.cifwk.raml.api.Specification;
import com.ericsson.cifwk.raml.api.Violation;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

public class SimpleReport implements Report {

    private Specification spec;
    private List<Violation> violations = newArrayList();

    public SimpleReport(Specification spec) {
        this.spec = spec;
    }

    @Override
    public String name() {
        return spec.getName();
    }

    @Override
    public List<Violation> violations() {
        return violations;
    }

    @Override
    public void accept(Violation message) {
        violations.add(message);
    }
}
