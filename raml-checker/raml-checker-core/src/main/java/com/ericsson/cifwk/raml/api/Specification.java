package com.ericsson.cifwk.raml.api;

import guru.nidi.ramltester.RamlDefinition;
import org.raml.model.Raml;

public interface Specification {

    String getName();

    Raml getModel();

    RamlDefinition getDefinition();

}
