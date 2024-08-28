package com.ericsson.cifwk.raml.validator;

import com.ericsson.cifwk.raml.api.Report;
import com.ericsson.cifwk.raml.api.Specification;
import com.ericsson.cifwk.raml.api.Validator;
import com.ericsson.cifwk.raml.report.SimpleReport;
import com.ericsson.cifwk.raml.tree.DelegatingRamlVisitor;
import org.raml.model.Action;
import org.raml.model.Raml;
import org.raml.model.Resource;

import static com.ericsson.cifwk.raml.tree.RamlWalker.walk;

public abstract class SimpleValidator implements Validator {

    @Override
    public Report validate(Specification spec) {
        SimpleReport report = new SimpleReport(spec);
        walk(spec.getModel(), new DelegatingRamlVisitor(
                model -> validateModel(model, report),
                resource -> validateResource(resource, report),
                action -> validateAction(action, report)
        ));
        return report;
    }

    protected void validateModel(Raml model, Report report) {
        // do nothing
    }

    protected void validateResource(Resource resource, Report report) {
        // do nothing
    }

    protected void validateAction(Action action, Report report) {
        // do nothing
    }
}
