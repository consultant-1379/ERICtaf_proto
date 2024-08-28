package com.ericsson.cifwk.raml.spec;

import com.ericsson.cifwk.raml.api.Specification;
import guru.nidi.ramltester.RamlDefinition;
import org.raml.model.Raml;

public class SimpleSpecification implements Specification {

    private final String name;
    private final RamlDefinition definition;

    public SimpleSpecification(String name, RamlDefinition definition) {
        this.name = name;
        this.definition = definition;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Raml getModel() {
        return definition.getRaml();
    }

    @Override
    public RamlDefinition getDefinition() {
        return definition;
    }
}
